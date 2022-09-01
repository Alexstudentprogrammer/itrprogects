package com.example.course_project.activity;

import com.example.course_project.userInfo.User;
import com.example.course_project.userInfo.UserRole;
import com.example.course_project.userInfo.UserService;
import com.example.course_project.user_collections.Comment;
import com.example.course_project.user_collections.Item;
import com.example.course_project.user_collections.repositories.CommentRepository;
import com.example.course_project.user_collections.services.ItemService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
@AllArgsConstructor
public class LikeCommentController {

    private final ItemService itemService;
    private final UserService userService;
    private final CommentRepository commentRepository;
    @PostMapping("collections/item/like/")
    public String like(@RequestParam("id") Long id){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findByUsername(authentication.getName()).get();
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
    public void deleteComment(@RequestParam("id") Long id, @RequestParam("itemId") Long itemId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findByUsername(authentication.getName()).get();
        Comment comment = commentRepository.findCommentByCommentId(id).get();
        if(user.getUsername().equals(comment.getAuthorOfComment()) || user.getRole() == UserRole.ADMIN) {
            userService.removeComment(comment.getCommentId(),itemId);
        }
    }
}
