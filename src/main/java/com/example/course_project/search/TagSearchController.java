package com.example.course_project.search;

import com.example.course_project.activity.LikeContainer;
import com.example.course_project.search.models.SearchProduct;
import com.example.course_project.userInfo.User;
import com.example.course_project.userInfo.UserRole;
import com.example.course_project.userInfo.UserService;
import com.example.course_project.user_collections.Item;
import com.example.course_project.user_collections.Tag;
import com.example.course_project.user_collections.services.ItemService;
import com.example.course_project.user_collections.services.TagService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/tag/search")
@AllArgsConstructor
public class TagSearchController {

    private final TagService tagRepository;
    private final ItemService itemService;
    private final SearchService searchService;
    private final UserService userService;
    @GetMapping("/{tags}")
    public List<LikeContainer> getByTags(@PathVariable("tags") String tags) {
        List<String> list = Arrays.asList(tags.split(","));
        List<LikeContainer> ret = new LinkedList<>();
        List<Tag> tag = new LinkedList<>();
        User user = getCurrentUser();
        for(String s :  list){
            tag.add(tagRepository.findByName(s));
        }
        for(Item i : itemService.findByTags(tag)){
            ret.add(new LikeContainer(i, i.getLiked().contains(user)));
        }
        return ret;
    }

    @PostMapping("/data/")
    public List<LikeContainer> searchItems(@RequestParam("val") String data){
        List<LikeContainer> ret = new LinkedList<>();
        User user = getCurrentUser();
        List<SearchProduct> search = searchService.findByData(data);
        for(SearchProduct s : search){
            Item i = itemService.findItemById(Long.parseLong(s.getPk())).get();//TODO use same in others
            ret.add(new LikeContainer(i,i.getLiked().contains(user)));
        }
        return ret;
    }
    @PostMapping("/data/user")
    public List<LikeContainer> searchItemsOfUser(@RequestParam("val") String data, @RequestParam("id")Long id){
        User user = getCurrentUser();
        List<SearchProduct> search;
        List<LikeContainer> ret = new LinkedList<>();
        if(user.getRole() == UserRole.USER) {
             search = searchService.findByUserIdAndData(data, id);
        }else{
             search = searchService.findByData(data);
        }
        for(SearchProduct s : search){
            Item i = itemService.findItemById(Long.parseLong(s.getPk())).get();
            ret.add(new LikeContainer(i, i.getLiked().contains(user)));
        }
        return ret;
    }
    @PostMapping("/data/collection/")
    public List<LikeContainer> searchItemsOfCollection(@RequestParam("val") String data, @RequestParam("name")String name){
        List<LikeContainer> ret = new LinkedList<>();
        User user = getCurrentUser();

        List<SearchProduct> search = searchService.findByCollectionAndData(data,name);
        for(SearchProduct s : search){
            Item i = itemService.findItemById(Long.parseLong(s.getPk())).get();
            ret.add(new LikeContainer(i, i.getLiked().contains(user)));
        }
        return ret;
    }

    private User getCurrentUser(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Optional<User> user = userService.findByUsername(auth.getName());
        return user.isPresent() ? user.get() : new User();
    }
}
