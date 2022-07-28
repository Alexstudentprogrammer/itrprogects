package com.example.demo1.Services;

import com.example.demo1.Models.Message;
import com.example.demo1.Models.User;
import com.example.demo1.Repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {

    private final MessageService service;
    private final UserRepository repository;

    public Long getOrRegisterUser(String name){

        Optional<User> user = repository.findUserByName(name);
        if(!user.isPresent()) {
            repository.save(new User(
                    name
            ));
        }
        return repository.findUserByName(name).get().getId();
    }

    public Long getUser(String name){

        Optional<User> user = repository.findUserByName(name);
        if(!user.isPresent()) {
            return (long)-1;
        }
        return repository.findUserByName(name).get().getId();
    }

    public String getUser(Long id){
        Optional<User> user = repository.findUserById(id);
        if(user.isPresent()){
            return user.get().getName();
        }
        return null;
    }

    public List<User> getAll(){
        return repository.findAll();
    }



}
