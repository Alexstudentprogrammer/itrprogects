package com.example.course_project.search;



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
    private String pK;
    private String collectionName;
    private String userId;
    private String data;

}
