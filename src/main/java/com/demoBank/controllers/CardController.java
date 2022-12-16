package com.demoBank.controllers;

import com.demoBank.entities.Card;
import com.demoBank.entities.User;
import com.demoBank.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;
import java.util.Objects;

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



    @PostMapping("/send")
    public String send(@ModelAttribute("sum") String sum,
                       @ModelAttribute("number") String number,
                       Model model, Principal principal){
        Card card = userService.findByUsername(principal.getName()).getCard();

        double dSum = 0.0;
        try {
            dSum = Double.parseDouble(sum);
        } catch (NumberFormatException e){
            model.addAttribute("sendMessage", "Неверно набрана сумма");
            System.out.println("Неверно набрана сумма:  " + sum);
        }

        if (Objects.equals(card.getNumber(), number)){
            model.addAttribute("sendMessage", "Неверно набран номер карты");
            System.out.println("Неверно набран номер карты");
            return "redirect:/";
        }
        if (dSum > card.getBalance()){
            model.addAttribute("sendMessage", "Недостаточно средств");
            System.out.println("Недостаточно средств");
            return "redirect:/";
        }
        if (!userService.send(dSum,number,principal)){
            model.addAttribute("sendMessage", "Неверно набран номер карты");
            System.out.println("Неверно набран номер карты:  " + number);
            return "redirect:/";
        }

        card.setBalance(card.getBalance() - dSum);
        model.addAttribute("sendMessage", "перевод выполнен");
        System.out.println("перевод выполнен");
        return "redirect:/";
    }
}
