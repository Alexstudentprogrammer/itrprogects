package com.example.course_project.activity.socket;

import lombok.*;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
@Setter
@ToString
public class CommentPostRequest {

    private String author;
    private String content;
    private Long itemId;
    private Long id;
    private Long userId;
}
