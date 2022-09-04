package com.example.course_project.homePage;

import com.example.course_project.userInfo.User;
import com.example.course_project.userInfo.UserRepository;
import com.example.course_project.userInfo.UserService;
import com.example.course_project.userInfo.costumAuth.CostumClient;
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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
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
    public String getAllCollections(Model model, @PathVariable("option") int opt, @AuthenticationPrincipal OAuth2User oAuth2User){
        User user = getCurrentUser(oAuth2User);
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
        if(user == null) {
            user1 = new User();
        }else{
            user1 = user;
        }

        model.addAttribute("user",user1);
        model.addAttribute("load_items", load);
        model.addAttribute("items", items);
        model.addAttribute("cols",col);
        model.addAttribute("tags",tags);
        return "home";
    }

    @GetMapping("collections/")
    public String getMyCollections(@RequestParam Long id, Model model, @AuthenticationPrincipal OAuth2User oAuth2User){
        User user = getCurrentUser(oAuth2User);
        Optional<UserCollection> collection = userCollectionService.findById(id);
        List<Item> items = itemService.findItemsOf(id);
        if(collection.isPresent())
            model.addAttribute("col",collection.get());
        if(user != null) {
            model.addAttribute("user", user);
        }else{
            model.addAttribute("user", new User());
        }
        model.addAttribute("items", items);
        return "collection_details";
    }

    @GetMapping("collections/item/{id}")
    public String detailedItemView(@PathVariable("id") Long id, Model model, @AuthenticationPrincipal OAuth2User oAuth2User){
       User user = getCurrentUser(oAuth2User);

      Item item = itemService.findItemById(id).get();
      model.addAttribute("item",item);
      model.addAttribute("comments",item.getComments());
        if(user!=null) {
            model.addAttribute("user", user);
        }else{
            model.addAttribute("user", new User());
        }
        return "item_detailed_view";
    }

    private User getCurrentUser(OAuth2User oAuth2User){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = null;
        if(authentication.getPrincipal() != null && authentication.getPrincipal() instanceof CostumClient){
             CostumClient cred  = (CostumClient) authentication.getPrincipal();
             username = cred.getUsername();
        }else{
             username = authentication.getName();
        }
        Optional<User> user = userService.findByUsername(username);
        return  !user.isPresent() ? new User():user.get();
    }
}
