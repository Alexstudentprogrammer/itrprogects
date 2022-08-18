package com.example.course_project.search;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class SearchService {

    private SearchRepository repository;

    public void add(SearchProduct product){
        repository.save(product);
    }

    public void remove(SearchProduct product){
        repository.delete(product);
    }

    public List<SearchProduct> findByData(String data){
        return repository.findByDataContaining(data);
    }
}
