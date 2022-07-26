package com.example.demo.registration;

import com.example.demo.UserInfo.User;
import com.example.demo.UserInfo.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

@Service
@AllArgsConstructor
public class RegistrationService {

    private final UserService service;

    public int register(RegistrationRequest request){
        String registerDate = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
        return service.signUp(
                new User(
                    request.getFirstName(),
                    request.getLastName(),
                    request.getEmail(),
                    request.getPassword(),
                    registerDate
                )
        );
    }
}
