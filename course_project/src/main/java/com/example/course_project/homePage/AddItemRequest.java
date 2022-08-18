package com.example.course_project.homePage;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class AddItemRequest {

    private String name;
    private String tags;
    private String data;
    private Long collectionId;
}
