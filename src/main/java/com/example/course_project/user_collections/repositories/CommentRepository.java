package com.example.course_project.user_collections.repositories;

import com.example.course_project.user_collections.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;

@Repository
@Transactional
public interface CommentRepository extends JpaRepository<Comment,Long> {

    Optional<Comment> findCommentByCommentId(Long id);
    @Modifying
    @Transactional
    void deleteCommentByCommentId(Long id);

}
