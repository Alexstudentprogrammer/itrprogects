package com.example.demo1.Services;

import com.example.demo1.Models.Message;
import com.example.demo1.Repositories.MessageRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
@AllArgsConstructor
public class MessageService {

    private final MessageRepository repository;

    public Collection<Message> getAll(Long userId){
        return repository.getMessagesForUser(userId);
    }

    public Message save(Message m){
        return repository.save(m);
    }
    public void flush(){
        repository.flush();
    }
}
