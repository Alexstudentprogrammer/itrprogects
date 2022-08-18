package com.example.course_project.registration;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(path = "/signup")
@AllArgsConstructor
public class RegistrationController {

    private final RegistrationService service;

    @GetMapping
    public String signupForm(Model model){
        return "register";
    }

    @PostMapping
    public String sumbitForm(@ModelAttribute RegistrationRequest request){
        int a = service.register(request);
        if(a == 1){
            return "/login";
        }else{
            //return "redirect:https://itrproject1.herokuapp.com/api/signup/failed";//TODO handle failed registration
            return "/signup";
        }
    }
}
