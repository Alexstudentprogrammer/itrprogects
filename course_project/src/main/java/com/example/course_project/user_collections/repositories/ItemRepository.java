package com.example.course_project.user_collections.repositories;

import com.example.course_project.user_collections.Item;
import com.example.course_project.user_collections.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findByTagsIn(List<Tag> tag);

    @Query(value = "SELECT * FROM item WHERE item.id = :id", nativeQuery = true)
    List<Item> findItemsOf(@Param("id") Long id);
}
