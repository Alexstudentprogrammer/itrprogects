package com.example.demo1.Templates;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/")
@AllArgsConstructor
public class TemplateController {

    @GetMapping
    public String index(){
        return "login";
    }


}
