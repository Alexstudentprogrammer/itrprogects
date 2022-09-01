package com.example.course_project.user_collections.services;

import com.example.course_project.user_collections.Tag;
import com.example.course_project.user_collections.repositories.TagRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class TagService {

    private final TagRepository repository;

    public List<Tag> findAll(){
        return repository.findAll();
    }
    public Tag findByName(String name){
        Optional<Tag> t = repository.findTagByName(name);
        return t.isPresent() ? t.get() : null;
    }
}
