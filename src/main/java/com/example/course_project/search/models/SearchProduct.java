package com.example.course_project.search.models;



import lombok.*;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.Id;

@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
@Document(indexName = "item_search")
public class SearchProduct {

    @Id
    private String id;
    private String pk;
    private String collectionId;

    //user id for to search among specified user collections
    //TODO Handle search for admins
    private String userId;

    //concatenation of everything, including collection name
    private String data;

}
