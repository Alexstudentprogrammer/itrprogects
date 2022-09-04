package com.example.course_project.search.models;

import lombok.*;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.Id;

@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
@Document(indexName = "comment_search")
public class CommentSearchProduct {

    @Id
    private String id;
    private String pk;
    private String userid;
    private String collectionid;
    private String data;
    private String commentid;

}
