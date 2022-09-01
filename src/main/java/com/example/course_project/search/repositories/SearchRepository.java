package com.example.course_project.search.repositories;

import com.example.course_project.search.models.SearchProduct;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SearchRepository extends ElasticsearchRepository<SearchProduct,String> {
    List<SearchProduct> findByDataContaining(String s);
    Optional<SearchProduct> findByPk(String pk);

    void deleteById(String id);
    List<SearchProduct> findAll();

    List<SearchProduct> findByCollectionIdAndDataContaining(String id, String data);
    List<SearchProduct> findByUserIdAndDataContaining(String userId, String data);


}
