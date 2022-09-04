package com.example.course_project.search.repositories;

import com.example.course_project.search.models.CollectionSearchProduct;
import com.example.course_project.search.models.SearchProduct;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;
import java.util.Optional;

public interface CollectionSearchRepository extends ElasticsearchRepository<CollectionSearchProduct,String> {

    List<CollectionSearchProduct> findByNameContaining(String name);
    List<CollectionSearchProduct> findByNameContainingAndUserid(String name, String id);
    Optional<CollectionSearchProduct> findByPk(String s);

    void deleteAll();
    List<CollectionSearchProduct> findAll();

}
