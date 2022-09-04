package com.example.course_project.user_collections.repositories;

import com.example.course_project.user_collections.Item;
import com.example.course_project.user_collections.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findByTagsIn(List<Tag> tag);

    Optional<Item> findItemByItemId(Long id);

    @Query(value = "SELECT * FROM item WHERE item.id = :id", nativeQuery = true)
    List<Item> findItemsOf(@Param("id") Long id);

    @Query("SELECT i FROM Item i ORDER BY i.itemId DESC")
    List<Item> getLatest();

    @Query(value = "SELECT * FROM item WHERE item.id = :id ORDER BY name", nativeQuery = true)
    List<Item> getSorted(@Param("id") Long id);

    List<Item> findByTagsInAndCollectionId(List<Tag> tag, Long id);
}
