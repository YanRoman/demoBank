package com.demoBank.controllers;

import com.demoBank.entities.Card;
import com.demoBank.entities.User;
import com.demoBank.repositories.CardRepository;
import com.demoBank.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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
    private final UserService userService;
    private final CardRepository cardRepository;
    @Autowired
    public HomeController(UserService userService, CardRepository cardRepository){
        this.userService = userService;
        this.cardRepository = cardRepository;
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
    private Card getUserCard(Principal principal){
        return userService.findByUsername(principal.getName()).getCard();
    }
    private String getCardDate(Principal principal){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-yy");
        return simpleDateFormat.format(userService.findByUsername(principal.getName()).getCard().getDate());

    }
    @GetMapping("/service")
    public String service(){
        return "service";
    }

    @PostMapping("/withdraw")
    public String withdraw(double amount, Principal principal, Model model) throws Exception {

        User user = userService.findByUsername(principal.getName());
        Card card = user.getCard();

        System.out.println(amount);
        if (amount > card.getBalance()){
            model.addAttribute("name", principal.getName());
            model.addAttribute("time", getTime());
            model.addAttribute("message", "Недостаточно средств :(");
            if (userService.findByUsername(principal.getName()).getCard() != null){
                model.addAttribute("card", getUserCard(principal));
                model.addAttribute("date", getCardDate(principal));
            }
            return "home";
        }
        card.setBalance(card.getBalance() - amount);

        if (card.getBalance() <= 60000){
            user.setIndebtedness(user.getIndebtedness() + amount);
        }
        cardRepository.save(card);
        userService.saveUser(user);
        return "redirect:/";
    }

    @PostMapping("/replenishment")
    public String replenishment(double amount, Principal principal, Model model) throws Exception {

        User user = userService.findByUsername(principal.getName());
        Card card = user.getCard();

        if (amount < 0){
            model.addAttribute("name", principal.getName());
            model.addAttribute("time", getTime());
            model.addAttribute("message", "Некоректный ввод данных :(");
            if (userService.findByUsername(principal.getName()).getCard() != null){
                model.addAttribute("card", getUserCard(principal));
                model.addAttribute("date", getCardDate(principal));
            }
            return "home";
        }

        card.setBalance(card.getBalance() + amount);
        user.setIndebtedness(user.getIndebtedness() - amount);

        if (user.getIndebtedness() < 0){
            user.setIndebtedness(0);
        }

        cardRepository.save(card);
        userService.saveUser(user);
        return "redirect:/";
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


}
