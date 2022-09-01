package com.example.course_project.homePage;

import com.example.course_project.userInfo.User;
import com.example.course_project.userInfo.UserRepository;
import com.example.course_project.userInfo.UserService;
import com.example.course_project.user_collections.Item;
import com.example.course_project.user_collections.Tag;
import com.example.course_project.user_collections.UserCollection;
import com.example.course_project.user_collections.repositories.ItemRepository;
import com.example.course_project.user_collections.repositories.TagRepository;
import com.example.course_project.user_collections.repositories.UserCollectionRepository;
import com.example.course_project.user_collections.services.ItemService;
import com.example.course_project.user_collections.services.TagService;
import com.example.course_project.user_collections.services.UserCollectionService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;


@Controller
@RequestMapping("/")
@AllArgsConstructor
public class HomePageController {
    private final UserService userService;
    private final UserCollectionService userCollectionService;
    private final ItemService itemService;
    private final TagService tagService;

    @GetMapping("all/{option}")
    public String getAllCollections(Model model, @PathVariable("option") int opt){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<User> user = userService.findByUsername(authentication.getName());
        User user1 = null;
        List<UserCollection> col = new LinkedList<>();
        List<Item> items = new LinkedList<>();
        if(opt == 1) {
             col = userCollectionService.getTop5();
        }else{
            items = itemService.getLatest();
        }
        List<Tag> tags = tagService.findAll();
        boolean load = items.size() > 0 ? true : false;
        if(!user.isPresent()) {
            user1 = new User();
        }else{
            user1 = user.get();
        }

        model.addAttribute("user",user1);
        model.addAttribute("load_items", load);
        model.addAttribute("items", items);
        model.addAttribute("cols",col);
        model.addAttribute("tags",tags);
        return "home";
    }

    @GetMapping("collections/")
    public String getMyCollections(@RequestParam Long id, Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<User> user = userService.findByUsername(authentication.getName());
        Optional<UserCollection> collection = userCollectionService.findById(id);
        List<Item> items = itemService.findItemsOf(id);
        if(collection.isPresent())
            model.addAttribute("col",collection.get());
        if(user.isPresent()) {
            model.addAttribute("user", user.get());
        }else{
            model.addAttribute("user", new User());
        }
        model.addAttribute("items", items);
        return "collection_details";
    }

    @GetMapping("collections/item/{id}")
    public String detailedItemView(@PathVariable("id") Long id, Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<User> user = userService.findByUsername(authentication.getName());

      Item item = itemService.findItemById(id).get();
      model.addAttribute("item",item);
      model.addAttribute("comments",item.getComments());
        if(user.isPresent()) {
            model.addAttribute("user", user.get());
        }else{
            model.addAttribute("user", new User());
        }
        return "item_detailed_view";
    }
    @PostMapping("lang/change")
    public String changeLanguage(@RequestParam("lang") String lang){
        User user = getCurrentUser();
        user.setLang(lang);
        userService.save(user);
        return "";
    }

    @PostMapping("skin/change")
    public String changeSkin(@RequestParam("skin") String skin){
        User user = getCurrentUser();
        user.setSkin(skin);
        userService.save(user);
        return "";
    }
    private User getCurrentUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findByUsername(authentication.getName()).get();
        return user;
    }
}
