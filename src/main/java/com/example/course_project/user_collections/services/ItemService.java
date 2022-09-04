package com.example.course_project.user_collections.services;

import com.example.course_project.user_collections.Item;
import com.example.course_project.user_collections.Tag;
import com.example.course_project.user_collections.repositories.ItemRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ItemService {

    private final ItemRepository repository;

    public List<Item> findItemsOf(Long id){
        return repository.findItemsOf(id);
    }

    public Optional<Item> findItemById(Long id){
        return repository.findItemByItemId(id);
    }

    public Item save(Item item){
        return repository.save(item);
    }
    public List<Item> findByTags(List<Tag> tags){return repository.findByTagsIn(tags);}

    public List<Item> getLatest(){return repository.getLatest();}
    public List<Item> getSortedOfCollection(Long id){
        List<Item> items = repository.getSorted(id);

        return items == null ? new LinkedList<>() : items;
    }
    public List<Item> filterByTagOfCollection(List<Tag> tags, Long id){
        List<Item> items = repository.findByTagsInAndCollectionId(tags,id);

        return items == null ? new LinkedList<>() : items;
    }
}
