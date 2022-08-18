package com.example.course_project.homePage;

import com.example.course_project.user_collections.Item;
import com.example.course_project.user_collections.Tag;
import com.example.course_project.user_collections.UserCollection;
import com.example.course_project.user_collections.repositories.ItemRepository;
import com.example.course_project.user_collections.repositories.TagRepository;
import com.example.course_project.user_collections.repositories.UserCollectionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;


@Controller
@RequestMapping("/")
@AllArgsConstructor
public class HomePageController {

    private final UserCollectionRepository repository;
    private final TagRepository tagRepository;//TODO replace with service
    private final ItemRepository itemRepository;

    @GetMapping
    public String getAllCollections(Model model){
        List<UserCollection> col = repository.findAll();
        List<Tag> tags = tagRepository.findAll();

        model.addAttribute("cols",col);
        model.addAttribute("tags",tags);
        return "home";
    }

    @GetMapping("collections/")
    public String getMyCollections(@RequestParam Long id, Model model){
        Optional<UserCollection> collection = repository.findById(id);
        List<Item> items = itemRepository.findItemsOf(id);
        if(collection.isPresent())
            model.addAttribute("col",collection.get());

        model.addAttribute("items", items);
        return "collection_details";
    }

}
