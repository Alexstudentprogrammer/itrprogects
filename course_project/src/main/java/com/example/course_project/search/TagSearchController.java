package com.example.course_project.search;

import com.example.course_project.user_collections.Item;
import com.example.course_project.user_collections.Tag;
import com.example.course_project.user_collections.repositories.ItemRepository;
import com.example.course_project.user_collections.repositories.TagRepository;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/tag/search")
@AllArgsConstructor
public class TagSearchController {

    private final TagRepository tagRepository;
    private final ItemRepository itemRepository;

    @GetMapping("/{tags}")
    public List<Item> getByTags(@PathVariable("tags") String tags) {
        List<String> list = List.of(tags.split(","));
        List<Tag> tag = new LinkedList<>();
        for(String s :  list){
            tag.add(tagRepository.findTagByName(s).get());
        }
        return itemRepository.findByTagsIn(tag);

    }


}
