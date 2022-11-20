package com.demoBank.controllers;

import com.demoBank.domain.Role;
import com.demoBank.domain.UserBank;
import com.demoBank.repos.UserBankRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Collections;

@Controller
public class RegistrationController {
    @Autowired
    private UserBankRepo userRepo;

    @GetMapping("/registration")
    public String registration(){
        return "security/registration";
    }

    @PostMapping("/registration")
    public String addUser(UserBank user, Model model) {
        UserBank userFromDb = userRepo.findByUsername(user.getUsername());

        if (userFromDb != null) {
            model.addAttribute("message", "User exists!");
            return "security/registration";
        }

        user.setActive(true);
        user.setRoles(Collections.singleton(Role.USER));
        userRepo.save(user);

        return "redirect:security/login";
    }
}
