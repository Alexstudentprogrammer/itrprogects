package com.example.course_project.search.models;

import lombok.*;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.Id;

@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
@Document(indexName = "collection_search")
public class CollectionSearchProduct {
    @Id
    private String id;
    private String name;
    private String pk;
    private String userid;
}
