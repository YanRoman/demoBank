package com.demoBank.controllers;

import com.demoBank.entities.Card;
import com.demoBank.repositories.CardRepository;
import com.demoBank.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class CardController {
    private final UserService userService;
    @Autowired
    public CardController(UserService userService){
        this.userService = userService;
    }

    @GetMapping("/newCard")
    public String newCard(Principal principal, Model model){
        if (!userService.addCard(principal)){
            model.addAttribute("message", "У вас уже есть карта");
        }
        return "redirect:/";
    }

    @GetMapping("/deleteCard")
    public String deleteCard(Principal principal){
        userService.deleteCard(principal);
        return "redirect:/";
    }
}
