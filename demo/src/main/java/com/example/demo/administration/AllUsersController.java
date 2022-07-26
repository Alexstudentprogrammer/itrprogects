package com.example.demo.administration;

import com.example.demo.UserInfo.User;
import com.example.demo.UserInfo.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/")
@AllArgsConstructor
public class AllUsersController {

private final UserService service;

    @GetMapping("/getAll")
    public String getRegisterView(Model model){
        if(Authenticated()) {
        List<User> all = (List<User>)service.getAll();
        model.addAttribute("users", all);
        return "admin";
        }else{
            return "redirect:https://itrproject1.herokuapp.com/logout";
        }
    }

    private boolean Authenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<User> user = service.findByEmail(authentication.getName());
        if(user.isPresent() && user.get().isAccountNonLocked()){
            return true;
        }
        return false;
    }
}
