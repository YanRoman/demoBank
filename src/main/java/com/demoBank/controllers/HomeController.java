package com.demoBank.controllers;

import com.demoBank.entities.Card;
import com.demoBank.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.security.Principal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
public class HomeController {
    private final UserService userService;
    @Autowired
    public HomeController(UserService userService){
        this.userService = userService;
    }
    @GetMapping("/")
    public String home(Principal principal, Model model) throws Exception {
        model.addAttribute("name", principal.getName());
        model.addAttribute("time", getTime());

        if (userService.findByUsername(principal.getName()).getCard() != null){
            model.addAttribute("card", getUserCard(principal));
            model.addAttribute("date", getCardDate(principal));
        }

        return "home";
    }

    @GetMapping("/service")
    public String service(){
        return "service";
    }

    private String getTime() throws Exception {
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("HH");
        double hour = Double.parseDouble(dateFormat.format(date));
        try {
            if (hour/4 < 1.5) {
                return "Доброй ночи";
            } else if (hour/4 < 3){
                return "Доброе утро";
            } else if(hour/4 < 4.5){
                return "Добрый день";
            } else {
                return "Добрый вечер";
            }
        } catch (Exception e){
            throw new Exception("Неверное системное время");
        }
    }
    private Card getUserCard(Principal principal){
        return userService.findByUsername(principal.getName()).getCard();
    }
    private String getCardDate(Principal principal){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-yy");
        return simpleDateFormat.format(userService.findByUsername(principal.getName()).getCard().getDate());

    }

}
