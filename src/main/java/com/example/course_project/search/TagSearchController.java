package com.example.course_project.search;

import com.example.course_project.activity.LikeContainer;
import com.example.course_project.search.models.SearchProduct;
import com.example.course_project.userInfo.User;
import com.example.course_project.userInfo.UserRole;
import com.example.course_project.userInfo.UserService;
import com.example.course_project.userInfo.costumAuth.CostumClient;
import com.example.course_project.user_collections.Item;
import com.example.course_project.user_collections.Tag;
import com.example.course_project.user_collections.services.ItemService;
import com.example.course_project.user_collections.services.TagService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
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
    public List<LikeContainer> getByTags(@PathVariable("tags") String tags,@AuthenticationPrincipal OAuth2User oAuth2User) {
        List<String> list = Arrays.asList(tags.split(","));
        List<LikeContainer> ret = new LinkedList<>();
        List<Tag> tag = new LinkedList<>();
        User user = getCurrentUser(oAuth2User);
        for(String s :  list){
            tag.add(tagRepository.findByName(s));
        }
        for(Item i : itemService.findByTags(tag)){
            ret.add(new LikeContainer(i, i.getLiked().contains(user)));
        }
        return ret;
    }

    @PostMapping("/data/")
    public List<LikeContainer> searchItems(@RequestParam("val") String data,@AuthenticationPrincipal OAuth2User oAuth2User){
        List<LikeContainer> ret = new LinkedList<>();
        User user = getCurrentUser(oAuth2User);
        List<SearchProduct> search = searchService.findByData(data);
        for(SearchProduct s : search){
            Item i = itemService.findItemById(Long.parseLong(s.getPk())).get();
            ret.add(new LikeContainer(i,i.getLiked().contains(user)));
        }
        return ret;
    }
    @PostMapping("/data/user")
    public List<LikeContainer> searchItemsOfUser(@RequestParam("val") String data, @RequestParam("id")Long id,@AuthenticationPrincipal OAuth2User oAuth2User){
        User user = getCurrentUser(oAuth2User);
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
    public List<LikeContainer> searchItemsOfCollection(@RequestParam("val") String data, @RequestParam("name")String name, @AuthenticationPrincipal OAuth2User oAuth2User){
        List<LikeContainer> ret = new LinkedList<>();
        User user = getCurrentUser(oAuth2User);

        List<SearchProduct> search = searchService.findByCollectionAndData(data,name);
        for(SearchProduct s : search){
            Item i = itemService.findItemById(Long.parseLong(s.getPk())).get();
            ret.add(new LikeContainer(i, i.getLiked().contains(user)));
        }
        return ret;
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
