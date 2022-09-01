package com.example.course_project.activity;

import com.example.course_project.user_collections.Item;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class LikeContainer {

    private Item item;
    private boolean isLiked;


}
