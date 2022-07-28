package com.example.demo1.sockets;

import com.example.demo1.Services.MessageService;
import com.example.demo1.Services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
@AllArgsConstructor
public class SocketConfig implements WebSocketConfigurer {

    private final UserService service;
    private final MessageService mService;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry webSocketHandlerRegistry) {
        webSocketHandlerRegistry.addHandler(new MessageHandler(service,mService), "/chat");
    }

}
