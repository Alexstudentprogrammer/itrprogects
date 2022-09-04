package com.example.course_project.search.repositories;

import com.example.course_project.search.models.CollectionSearchProduct;
import com.example.course_project.search.models.CommentSearchProduct;
import com.example.course_project.user_collections.Comment;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentSearchRepository extends ElasticsearchRepository<CommentSearchProduct,String> {

    List<CommentSearchProduct> findByDataContaining(String data);
    List<CommentSearchProduct> findByDataContainingAndUserid(String data, String id);
    List<CommentSearchProduct> findByDataContainingAndCollectionid(String data, String id);
    Optional<CommentSearchProduct> findByPk(String s);

    List<CommentSearchProduct> findAll();
    void deleteByCommentid(String id);
    Optional<CommentSearchProduct> findByCommentid(String id);
    List<CommentSearchProduct> findAllByUserid(String id);

}
