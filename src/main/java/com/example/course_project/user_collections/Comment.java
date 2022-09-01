package com.example.course_project.user_collections;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@ToString
@Entity
public class Comment {

    @SequenceGenerator(

            name = "comment_sequence",
            sequenceName = "comment_sequence",
            allocationSize = 1
    )
    @Id
    @GeneratedValue(

            strategy = GenerationType.SEQUENCE,
            generator = "comment_sequence"
    )
    private Long commentId;
    private String authorOfComment;
    private String contentOfComment;

    public String getData(){
        return " "+authorOfComment + " " +contentOfComment+" ";
    }
}
