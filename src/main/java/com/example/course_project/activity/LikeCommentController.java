package com.example.course_project.activity;

import com.example.course_project.userInfo.User;
import com.example.course_project.userInfo.UserRole;
import com.example.course_project.userInfo.UserService;
import com.example.course_project.userInfo.costumAuth.CostumClient;
import com.example.course_project.user_collections.Comment;
import com.example.course_project.user_collections.Item;
import com.example.course_project.user_collections.repositories.CommentRepository;
import com.example.course_project.user_collections.services.ItemService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/")
@AllArgsConstructor
public class LikeCommentController {

    private final ItemService itemService;
    private final UserService userService;
    private final CommentRepository commentRepository;
    @PostMapping("collections/item/like/")
    public String like(@RequestParam("id") Long id,@AuthenticationPrincipal OAuth2User oAuth2User){
        User user = getCurrentUser(oAuth2User);
        Item item = itemService.findItemById(id).get();
        if(item.getLiked().contains(user)){
            item.getLiked().remove(user);
        }else{
            item.getLiked().add(user);
        }
        itemService.save(item);
        return "{\"likes\":" + item.getLiked().size()+"}";
    }
    @DeleteMapping("collections/item/comment/delete/")
    public void deleteComment(@RequestParam("id") Long id, @RequestParam("itemId") Long itemId, @AuthenticationPrincipal OAuth2User oAuth2User) {
        User user = getCurrentUser(oAuth2User);
        Comment comment = commentRepository.findCommentByCommentId(id).get();
        if(user.getUsername().equals(comment.getAuthorOfComment()) || user.getRole() == UserRole.ADMIN) {
            userService.removeComment(comment.getCommentId(),itemId);
        }
    }
    @PostMapping("lang/change")
    public void changeLanguag(@RequestParam("lang") String lang, @AuthenticationPrincipal OAuth2User oAuth2User){
        User user = getCurrentUser(oAuth2User);
        user.setLang(lang);
        userService.save(user);

    }

    @PostMapping("skin/change")
    public void changeSki(@RequestParam("skin") String skin, @AuthenticationPrincipal OAuth2User oAuth2User) {
        User user = getCurrentUser(oAuth2User);
        user.setSkin(skin);
        userService.save(user);
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
