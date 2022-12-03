package com.demoBank.controllers;

import com.demoBank.entities.User;
import com.demoBank.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;

@Controller
public class SettingsController {

    private UserService userService;
    @Autowired
    public SettingsController(UserService userService){
        this.userService = userService;
    }

    @GetMapping("/settings")
    public String change(Model model){
        model.addAttribute("username", new String());
        return "settings";
    }

    @PostMapping("/changeUsername")
    public String changeUsername(@ModelAttribute("username") String username, Principal principal){
        System.out.println(username);
        userService.setUsername(username, principal);
        return "redirect:/";
    }
}
