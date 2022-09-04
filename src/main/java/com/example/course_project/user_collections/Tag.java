package com.example.course_project.user_collections;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@ToString
@Entity
public class Tag {

    @SequenceGenerator(

            name = "tag_sequence",
            sequenceName = "tag_sequence",
            allocationSize = 1
    )
    @Id
    @GeneratedValue(

            strategy = GenerationType.SEQUENCE,
            generator = "tag_sequence"
    )
    private Long tagId;
    @Column(unique = true)
    private String name;

    public String getData(){
        return " "+name+" ";
    }
}
