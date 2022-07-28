package com.example.demo1.Repositories;

import com.example.demo1.Models.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface MessageRepository extends JpaRepository<Message,Long> {

    Optional<Message> findMessageByMessageId(Long id);

    @Query(value = "SELECT * FROM Message m where m.recipient_id = ?1",
           nativeQuery = true)
    Collection<Message> getMessagesForUser(Long id);


}
