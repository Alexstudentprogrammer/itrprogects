package com.example.course_project.administration;

import com.example.course_project.userInfo.User;
import com.example.course_project.userInfo.UserRole;
import com.example.course_project.userInfo.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/")
@AllArgsConstructor
public class administrationController {

    private final UserService service;

    @GetMapping("users/admin")
    public String getAllUsers(Model model){
        if(Authenticated()) {
            User user = (User) service.loadUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
            List<User> all = (List<User>)service.getAll();
            model.addAttribute("users", all);
            model.addAttribute("lang", user.getLang());
            model.addAttribute("skin", user.getSkin());
            return "admin";
        }else{
            return "redirect:https://itrcourseproject.herokuapp.com/login";
        }
    }

    private boolean Authenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<User> user = service.findByUsername(authentication.getName());
        if(user.isPresent() && user.get().isAccountNonLocked() && user.get().getRole() == UserRole.ADMIN){
            return true;
        }
        return false;
    }
}
