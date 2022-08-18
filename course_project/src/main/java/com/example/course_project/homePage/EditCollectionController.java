package com.example.course_project.homePage;

import com.example.course_project.userInfo.User;
import com.example.course_project.userInfo.UserRepository;
import com.example.course_project.userInfo.UserService;
import com.example.course_project.user_collections.Item;
import com.example.course_project.user_collections.Theme;
import com.example.course_project.user_collections.UserCollection;
import com.example.course_project.user_collections.repositories.ItemRepository;
import com.example.course_project.user_collections.repositories.TagRepository;
import com.example.course_project.user_collections.repositories.UserCollectionRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Controller
@RequestMapping("/mycollections/")
@AllArgsConstructor
public class EditCollectionController {

    private final UserCollectionRepository userCollectionRepository;
    private final UserRepository userRepository;
    private final UserService service;
    private final TagRepository tagRepository;
    private final ItemRepository itemRepository;

    @GetMapping
    public String getAllCollections(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findUserByUsername(authentication.getName()).get();
        List<UserCollection> col = userCollectionRepository.findAllUserCollections(user.getUserId());
        model.addAttribute("cols",col);
        return "edit_collections";
    }

    @GetMapping("/add")
    public String addCollectionsPage(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findUserByUsername(authentication.getName()).get();
        model.addAttribute("user_id",user.getUserId());
        return "addCollection";
        }


    @PostMapping("/add")
    public String createCollection(@ModelAttribute AddCollectionRequest request, @RequestParam("image") MultipartFile image){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findUserByUsername(authentication.getName()).get();
        UserCollection col = new UserCollection();
        col.setUser(user);
        col.setItems(new LinkedList<>());
        col.setName(request.getName());
        col.setTheme(Theme.valueOf(request.getTheme().toUpperCase()));
        //col.setImage(); use cloudinary service
        col.setInformation(request.getDesc());
        user.getUserCollections().add(col);
        userRepository.save(user);

        return "redirect:/mycollections/";

    }
    @GetMapping("/edit/{id}")
    public String editCollection(@PathVariable("id") Long id, Model model){
    UserCollection col = userCollectionRepository.findUserCollectionById(id).get();
    model.addAttribute("col",col);
    model.addAttribute("items", col.getItems());
    System.out.println(col.getItems());
    return "edit_my_collection";

    }
    @PostMapping("/edit")
    public String saveCollectionChanges(@ModelAttribute AddCollectionRequest request, @RequestParam("id") Long id){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findUserByUsername(authentication.getName()).get();
        UserCollection col = userCollectionRepository.findById(id).get();
        if(user.getUserId() == col.getUser().getUserId()) {
            col.setName(request.getName());
            col.setTheme(Theme.valueOf(request.getTheme().toUpperCase()));
            col.setInformation(request.getDesc());
            userCollectionRepository.save(col);
            return "redirect:/mycollections/";
        }
        return "";
    }

    @PostMapping("/add/item/save")
    public String saveItemChanges(@ModelAttribute AddItemRequest request){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findUserByUsername(authentication.getName()).get();
        UserCollection col = userCollectionRepository.findById(request.getCollectionId()).get();
        if(user.getUserId() == col.getUser().getUserId()) {
            Item item = new Item();
            item.setComments(new LinkedList<>());
            item.setName(request.getName());
            item.setLiked(new LinkedHashSet<>());
            item.setData(request.getData());
            service.addItem(col.getId(),item,new HashSet<>(Arrays.asList(request.getTags().split(","))));

            return "redirect:/mycollections/";
        }
        return "";
        }
    @GetMapping("/add/item/{id}")
    public String createItem(@PathVariable("id") Long id, Model model){
        model.addAttribute("col_id",id);
        model.addAttribute("tags",tagRepository.findAll());
        return "add_item";
    }

    @GetMapping("edit/item/{id}")
    public String editItem(@PathVariable("id") Long id, Model model){
        model.addAttribute("item_id",id);
        model.addAttribute("tags",itemRepository.findById(id).get().getTags());
        return "edit_my_item";
    }

    @PostMapping("edit/item/save")
    public String editItem(@ModelAttribute AddItemRequest request, @RequestParam Long id){
        service.updateItem(id,new HashSet<>(Arrays.asList(request.getTags().split(","))),request.getName());
        return "redirect:/mycollections/";
    }
}















