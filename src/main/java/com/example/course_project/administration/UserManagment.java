package com.example.course_project.administration;

import com.example.course_project.userInfo.User;
import com.example.course_project.userInfo.UserRole;
import com.example.course_project.userInfo.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
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
                service.deleteUser(Long.parseLong(id));
            }
            try {
                res.sendRedirect("/users/admin");
            }catch (IOException e){
                e.printStackTrace();
            }
        }else{
            try {
                res.sendRedirect("/logout");
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
                res.sendRedirect("/users/admin");
            }catch (IOException e){
                e.printStackTrace();
            }
        }else{
            try {
                res.sendRedirect("/logout");
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }
    @PostMapping("/api/promote")
    public void promote(ManagmentRequest request,HttpServletResponse res){
        if(Authenticated()) {
            String ids[] = request.getIds().split(",");
            for (String id : ids) {
                service.promoteUser(Long.parseLong(id));
            }
            try {
                res.sendRedirect("/users/admin");
            }catch (IOException e){
                e.printStackTrace();
            }
        }else{
            try {
                res.sendRedirect("/logout");
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    @PostMapping("/api/downgrade")
    public void downgrade(ManagmentRequest request,HttpServletResponse res){
        if(Authenticated()) {
            String ids[] = request.getIds().split(",");
            for (String id : ids) {
                service.downgradeUser(Long.parseLong(id));
            }
            try {
                res.sendRedirect("/users/admin");
            }catch (IOException e){
                e.printStackTrace();
            }
        }else{
            try {
                res.sendRedirect("/logout");
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
                res.sendRedirect("/users/admin");
            }catch (IOException e){
                e.printStackTrace();
            }
        }else{
            try {
                res.sendRedirect("/logout");
            }catch (IOException e){
                e.printStackTrace();
            }
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