package com.demoBank.controllers;

import com.demoBank.entities.User;
import com.demoBank.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import javax.validation.Valid;

import java.util.Objects;

@Controller
public class RegistrationController{

    private final UserService userService;

    @Autowired
    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String login(){
        return "/auth/login";
    }

    @GetMapping("/registration")
    public String registrationForm(Model model){
        model.addAttribute("user", new User());
        return "/auth/registration";
    }


    @PostMapping("/registration")
    public String addUser(@Valid User user, BindingResult bindingResult, Model model){
        if(bindingResult.hasErrors()){
            model.addAttribute("user", user);
            return "auth/registration";
        }

        if (user.getPassword() != null && !Objects.equals(user.getPassword(), user.getPasswordConfirm())){
            model.addAttribute("message", "Пароли не совпадают");
            return "auth/registration";
        }

        if (userService.findByEmail(user.getEmail()) != null){
            model.addAttribute("message", "Пользователь с таким email уже существует");
            return "auth/registration";
        }

        if (userService.findByTelephone(user.getTelephone()) != null){
            model.addAttribute("message", "Пользователь с таким телефоном уже существует");
            return "auth/registration";
        }

        if (!userService.saveUser(user)){
            model.addAttribute("message", "Пользователь с таким именем уже существует");
            return "auth/registration";
        }


        return "redirect:/login";
    }
}
