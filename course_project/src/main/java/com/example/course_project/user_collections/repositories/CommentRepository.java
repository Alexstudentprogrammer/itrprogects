package com.example.course_project.user_collections.repositories;

import com.example.course_project.user_collections.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface CommentRepository extends JpaRepository<Comment,Long> {

}
