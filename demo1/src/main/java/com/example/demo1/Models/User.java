package com.example.demo1.Models;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@Entity
public class User {
    @Id
    @SequenceGenerator(
            name = "user_sequence",
            sequenceName = "user_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "user_sequence"
    )
    private  Long id;
    private  String name;

    @OneToMany
    @JoinColumn(
            name = "id",
            referencedColumnName = "id"
    )
    private List<Message> messages;

    public User(String name){
        this.name = name;
    }
}
