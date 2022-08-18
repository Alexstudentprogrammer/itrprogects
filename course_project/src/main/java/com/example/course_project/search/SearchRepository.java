package com.example.course_project.search;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface SearchRepository extends ElasticsearchRepository<SearchProduct,String> {
    List<SearchProduct> findByDataContaining(String s);
}
