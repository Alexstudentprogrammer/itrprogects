package com.example.course_project.user_collections;

import com.example.course_project.userInfo.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"items","user"})

@Entity
public class UserCollection {//do not name field as reserved keywords in MySQL
    @Id
    @GeneratedValue(

            strategy = GenerationType.SEQUENCE,
            generator = "user_collection_sequence"
    )
    @SequenceGenerator(

            name = "user_collection_sequence",
            sequenceName = "user_collection_sequence",
            allocationSize = 1
    )
    private Long id;
    private String name;
    private String information;
    @Column(length = 5000)
    private String infoHtml;
    @Enumerated(EnumType.STRING)
    private Theme theme;
    @OneToMany(
            cascade = {CascadeType.REFRESH,CascadeType.REMOVE,CascadeType.MERGE},
            orphanRemoval = true,
            fetch = FetchType.EAGER
    )
    @JoinColumn(
            name = "id",
            referencedColumnName = "id"
    )
    private List<Item> items;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private User user;

    private String image;
    @Column(length = 3000)
    private String data;

    public void prepareForDelete(){
        user = null;
    }
}
