package com.example.demo.security.LoginActivity;

import com.example.demo.UserInfo.User;
import com.example.demo.UserInfo.UserService;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UpdateLoginActivityService
        implements ApplicationListener<AuthenticationSuccessEvent> {

    private final UserService service;

    @Override
    public void onApplicationEvent(AuthenticationSuccessEvent event) {
        Long id = ((User)event.getAuthentication().getPrincipal()).getId();
         service.updateLogin(id);
    }
}
