package com.example.demo1.Templates;

import com.example.demo1.Models.Message;
import com.example.demo1.Models.User;
import com.example.demo1.Services.MessageService;
import com.example.demo1.Services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/messages")
@AllArgsConstructor
public class MessageTemplateController {

    private final MessageService service;
    private final UserService userService;


    @PostMapping
    public String view(MessageRequest request, Model model){
        Long id = userService.getOrRegisterUser(request.getName());
        List<Message> msgs = (List<Message>)service.getAll(id);
        List<User> users = (List<User>)userService.getAll();
        model.addAttribute("msgs",msgs);
        model.addAttribute("id",id);
        model.addAttribute("users",users);
        model.addAttribute("name", request.getName());
        return "messanger";
    }

//    @PostMapping("/send")
//    public String send(SendMessageRequest request){
//        Long recId = userService.getUser(request.getRecipientName());
//        if(recId != - 1){
//            Message m = new Message();
//            m.setRecipientId(recId);
//            m.setSenderId(request.getSenderId());
//            m.setTheme(request.getTheme());
//            m.setBody(request.getBody());
//            m.setRecipientName(userService.getUser(recId));
//            m.setSenderName(userService.getUser(request.getSenderId()));
//            m.setDate(new SimpleDateFormat("dd-MM-yyyy").format(new Date()));
//            service.save(m);
//            return "redirect:/messages/send";
//        }else{
//
//            return "redirect:/messages/send/failed";
//        }
//    }
    @GetMapping("/send")
    public String sent(){
        return "sent";
    }
    @GetMapping("/send/failed")
    public String failed(){
        return "failed";
    }
}
