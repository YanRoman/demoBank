package com.demoBank.controllers;

import com.demoBank.entities.Card;
import com.demoBank.entities.User;
import com.demoBank.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
public class HomeController {
    private final UserService userService;

    public HomeController(UserService userService){
        this.userService = userService;
    }

    @GetMapping("/")
    public String home(Principal principal, Model model){
        model.addAttribute("name", principal.getName());

        //  Системное время
        DateFormat dateFormat = new SimpleDateFormat("HH");
        Date date = new Date();
        double hour = Double.parseDouble(dateFormat.format(date));

        if (hour/4 < 1.5) {
            model.addAttribute("time", "Доброй ночи");
        } else if (hour/4 < 3){
            model.addAttribute("time", "Доброе утро");
        } else if(hour/4 < 4.5){
            model.addAttribute("time", "Добрый день");
        } else {
            model.addAttribute("time", "Добрый вечер");
        }

        List<Card> cards = userService.findByUsername(principal.getName()).getCards();
        model.addAttribute("cards", cards);

        return "home";
    }

    @GetMapping("/service")
    public String service(){
        return "service";
    }



}
