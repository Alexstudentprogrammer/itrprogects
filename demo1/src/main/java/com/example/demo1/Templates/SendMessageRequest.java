package com.example.demo1.Templates;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class SendMessageRequest {
    private final String recipientName;
    private final Long senderId;
    private final String theme;
    private final String body;
    private String senderName;
    private String date;
    private Long id;
}
