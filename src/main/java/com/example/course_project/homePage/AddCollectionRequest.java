package com.example.course_project.homePage;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class AddCollectionRequest {

    private String name;
    private String desc;
    private String theme;
    private String data;
    private Boolean delete;

}
