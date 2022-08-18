package com.example.course_project.user_collections;

import com.example.course_project.user_collections.repositories.UserCollectionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserCollectionService {

    private final UserCollectionRepository repository;
}
