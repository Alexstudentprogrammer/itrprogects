package com.example.course_project.user_collections;


import com.example.course_project.userInfo.User;
import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@ToString
@Entity
public class Item {
    @SequenceGenerator(

            name = "item_sequence",
            sequenceName = "item_sequence",
            allocationSize = 1
    )
    @Id
    @GeneratedValue(

            strategy = GenerationType.SEQUENCE,
            generator = "item_sequence"
    )
    private Long itemId;
    private String name;
    @ManyToMany(
            fetch = FetchType.EAGER
    )
    @JoinTable(

            name = "item_tag_map",
            joinColumns = @JoinColumn(
                    name = "item_id",
                    referencedColumnName = "itemId"
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "tag_id",
                    referencedColumnName = "tagId"
            )
    )
    private Set<Tag> tags = new HashSet<>();

    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.EAGER
    )
    @JoinColumn(
            name = "item_id",
            referencedColumnName = "itemId"
    )
    private List<Comment> comments = new LinkedList<>();

    @ManyToMany(
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER
    )
    @JoinTable(

            name = "item_user_like_map",
            joinColumns = @JoinColumn(
                    name = "item_id",
                    referencedColumnName = "itemId"
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "user_id",
                    referencedColumnName = "userId"
            )
    )
    Set<User> liked = new HashSet<>();
    private String data;
    public void prepareForDelete(){
        tags.clear();
        liked.clear();
    }

    public String prepareForSearch(){
        StringBuilder sb = new StringBuilder();
        sb.append(name);
        sb.append(" ");
        for(Tag t : tags){
            sb.append(t.getData());
        }
        for(Comment c : comments){
            sb.append(c.getData());
        }
        sb.append(" ");
        return sb.toString();
    }

}
