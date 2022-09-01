package com.example.course_project.activity.socket;

import com.example.course_project.userInfo.User;
import com.example.course_project.userInfo.UserService;
import com.google.gson.Gson;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommentActivity extends TextWebSocketHandler {

    private final UserService userService;
    List<WebSocketSession> sessions = Collections.synchronizedList(new ArrayList<>());

    public CommentActivity(UserService userService) {
        this.userService = userService;
    }


    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
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
            CommentPostRequest request = new Gson().fromJson((String) message.getPayload(), CommentPostRequest.class);


            Long comId = userService.addComment(session.getPrincipal().getName(), request.getContent(), request.getItemId());
            request.setId(comId);
            final String data = new Gson().toJson(request);
            for (WebSocketSession s : sessions) {
                try {
                    s.sendMessage(new TextMessage(data));
                } catch (Exception ex) {
                    synchronized (sessions) {
                        sessions.remove(s);
                    }
                }
        }
    }
}
