package com.example.course_project.registration;

import com.example.course_project.userInfo.User;
import com.example.course_project.userInfo.UserRole;
import com.example.course_project.userInfo.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;

@Service
@AllArgsConstructor
public class RegistrationService {

    private final UserService service;

    public int register(RegistrationRequest request){
        System.out.println(request.getUsername());

        return service.signUp(
                new User(
                        request.getFirstName(),
                        request.getLastName(),
                        request.getUsername(),
                        request.getPassword(),
                        UserRole.USER
                )
        );
    }
}
