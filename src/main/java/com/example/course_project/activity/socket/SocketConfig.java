package com.example.course_project.activity.socket;

import com.example.course_project.userInfo.UserService;
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


        @Override
        public void registerWebSocketHandlers(WebSocketHandlerRegistry webSocketHandlerRegistry) {
            webSocketHandlerRegistry.addHandler(new CommentActivity(service), "/comment/post");

        }

    }

