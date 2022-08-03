package com.example.demo2.Templates;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/")
public class TemplateController {

    @GetMapping
    public String getMainPage(){
        return "RandomData";
    }
}
