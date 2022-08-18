package com.example.course_project.user_collections.repositories;

import com.example.course_project.user_collections.UserCollection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface UserCollectionRepository extends JpaRepository<UserCollection,Long> {

    Optional<UserCollection> findUserCollectionById(Long id);
    @Query(nativeQuery = true,value = "SELECT * FROM user_collection WHERE user_id = :id")
    List<UserCollection> findAllUserCollections(@Param("id") Long id);
}
