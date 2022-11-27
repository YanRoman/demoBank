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
    private final CardRepository cardRepository;
    private final UserService userService;
    @Autowired
    public CardController(CardRepository cardRepository, UserService userService){
        this.cardRepository = cardRepository;
        this.userService = userService;
    }

    @GetMapping("/newCard")
    public String newCard(Principal principal){
        Card card = new Card();
        userService.addCard(principal, card);

        return "redirect:/";
    }
}
