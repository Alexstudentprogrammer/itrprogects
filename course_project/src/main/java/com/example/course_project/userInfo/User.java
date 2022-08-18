package com.example.course_project.userInfo;


import com.example.course_project.user_collections.UserCollection;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.*;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@ToString(exclude = "userCollections")
@Entity
public class User implements UserDetails {


    @SequenceGenerator(

            name = "user_sequence",
            sequenceName = "user_sequence",
            allocationSize = 1
    )
    @Id
    @GeneratedValue(

            strategy = GenerationType.SEQUENCE,
            generator = "user_sequence"
    )
    private Long userId;
    private String fName;
    private String lName;
    private String username;
    private String password;
    @Enumerated(EnumType.STRING)
    private UserRole role;
    private boolean isLocked = false;
    @OneToMany(
            fetch = FetchType.EAGER,
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @JoinColumn(
            name = "user_id",
            referencedColumnName = "userId"
    )
    List<UserCollection> userCollections = new ArrayList<>(); //TODO remove initialization

    public User(String fName,
                String lName,
                String username,
                String password,
                UserRole role) {
        this.fName = fName;
        this.lName = lName;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority sga = new SimpleGrantedAuthority(role.name());
        return Collections.singletonList(sga);
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !isLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
