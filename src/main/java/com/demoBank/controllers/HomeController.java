package com.demoBank.controllers;

import com.demoBank.entities.User;
import com.demoBank.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
public class HomeController {

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

        return "home";
    }

    @GetMapping("/admin")
    public String admin(){
        return "admin";
    }

}