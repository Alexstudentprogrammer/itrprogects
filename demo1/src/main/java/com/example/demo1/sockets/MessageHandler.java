package com.example.demo1.sockets;

import com.example.demo1.Models.Message;
import com.example.demo1.Services.MessageService;
import com.example.demo1.Services.UserService;
import com.example.demo1.Templates.MessageRequest;
import com.example.demo1.Templates.SendMessageRequest;
import com.google.gson.Gson;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class MessageHandler extends TextWebSocketHandler {

    private final UserService userService;
    private final MessageService mService;
    List<WebSocketSession> sessions = Collections.synchronizedList(new ArrayList<>());

    public MessageHandler(UserService service, MessageService mService) {
        userService = service;
        this.mService = mService;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // System.out.println();
        super.afterConnectionEstablished(session);
        sessions.add(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        super.afterConnectionClosed(session, status);
        sessions.remove(session);
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {

        super.handleMessage(session, message);
        for (WebSocketSession webSocketSession : sessions) {

            SendMessageRequest request = new Gson().fromJson((String) message.getPayload(), SendMessageRequest.class);
            Long recId = userService.getUser(request.getRecipientName());
            if (recId != -1) {
                Message m = new Message();
                m.setRecipientId(recId);
                m.setSenderId(request.getSenderId());
                m.setTheme(request.getTheme());
                m.setBody(request.getBody());
                m.setRecipientName(userService.getUser(recId));
                m.setSenderName(userService.getUser(request.getSenderId()));
                m.setDate(new SimpleDateFormat("dd-MM-yyyy").format(new Date()));
                m = mService.save(m);
                mService.flush();
                request.setId(m.getMessageId());
                System.out.println(request.getId());
                final String data = new Gson().toJson(request);

                try {
                    webSocketSession.sendMessage(new TextMessage(data));
                } catch(Exception ex) {
                    synchronized( sessions ) {
                        sessions.remove(webSocketSession);
                    }
                }
            }
        }

    }
}




