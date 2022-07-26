package com.example.demo.registration;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(path = "/api/signup")
@AllArgsConstructor
public class RegistrationController {

    private final RegistrationService service;

    @GetMapping
    public String signupForm(Model model){
        return "register";
    }

    @GetMapping("/failed")
    public String failed(){
     return "failed";
    }
    @PostMapping
    public String sumbitForm(@ModelAttribute RegistrationRequest request){
        int a = service.register(request);
        if(a == 1){
            return "redirect:https://itrproject1.herokuapp.com/login";
        }else{
            return "redirect:https://itrproject1.herokuapp.com/api/signup/failed";
        }
    }
}
