package com.example.demo.administration;

import com.example.demo.UserInfo.User;
import com.example.demo.UserInfo.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@RestController
@RequestMapping("/manage")
@AllArgsConstructor
public class UserManagment {

    private final UserService service;

    @PostMapping("/api/delete")
    public void delete(ManagmentRequest request, HttpServletResponse res){
        if(Authenticated()) {
        String ids[] = request.getIds().split(",");
        for(String id : ids){
            service.delete(Long.parseLong(id));
        }
            try {
                res.sendRedirect("https://itrproject1.herokuapp.com/getAll");
            }catch (IOException e){
                e.printStackTrace();
            }
        }else{
            try {
                res.sendRedirect("https://itrproject1.herokuapp.com/logout");
            }catch (IOException e){
                e.printStackTrace();
            }


        }

    }

    @PostMapping("/api/unlock")
    public void unlock(ManagmentRequest request,HttpServletResponse res){
        if(Authenticated()) {
            String ids[] = request.getIds().split(",");
            for (String id : ids) {
                service.unlockUser(Long.parseLong(id));
            }
            try {
                res.sendRedirect("https://itrproject1.herokuapp.com/getAll");
            }catch (IOException e){
                e.printStackTrace();
            }
        }else{
                try {
                    res.sendRedirect("https://itrproject1.herokuapp.com/logout");
                }catch (IOException e){
                    e.printStackTrace();
                }
            }

    }

    @PostMapping("/api/lock")
    public void lock(ManagmentRequest request, HttpServletResponse res){
        if(Authenticated()) {

            String ids[] = request.getIds().split(",");
            for (String id : ids) {
                service.lockUser(Long.parseLong(id));
            }
            try {
                res.sendRedirect("https://itrproject1.herokuapp.com/getAll");
            }catch (IOException e){
                e.printStackTrace();
            }
        }else{
            try {
                res.sendRedirect("https://itrproject1.herokuapp.com/logout");
            }catch (IOException e){
                e.printStackTrace();
            }
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
