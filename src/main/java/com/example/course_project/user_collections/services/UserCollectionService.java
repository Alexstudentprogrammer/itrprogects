package com.example.course_project.user_collections.services;

import com.example.course_project.user_collections.UserCollection;
import com.example.course_project.user_collections.repositories.UserCollectionRepository;
import lombok.AllArgsConstructor;
import org.apache.catalina.User;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserCollectionService {

    private final UserCollectionRepository repository;

    public List<UserCollection> findAll(){
        return repository.findAll();
    }

    public Optional<UserCollection> findById(Long id){
        return repository.findUserCollectionById(id);
    }

    public List<UserCollection> findAllUserCollection(Long id){
        return repository.findAllUserCollections(id);
    }

    public UserCollection save(UserCollection col){
        return repository.save(col);
    }

    public List<UserCollection> getTop5(){
        return repository.getTop(PageRequest.of(0,5));
    }

}
