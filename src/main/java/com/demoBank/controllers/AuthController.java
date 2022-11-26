package com.demoBank.controllers;

import com.demoBank.entities.User;
import com.demoBank.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.Objects;

@Controller
public class AuthController {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/login")
    public String login(){
        return "/auth/login";
    }

    @GetMapping("/registration")
    public String registrationForm(){
        return "/auth/registration";
    }


    //  TODO add role initiation
    @PostMapping("/registration")
    public String registration(@Valid User user, BindingResult bindingResult, Model model){

        User userFromDb = userRepository.findByUsername(user.getUsername());
        if(userFromDb != null){
            model.addAttribute("message", "Пользователь с таким логином уже существует");
            return "auth/registration";
        }

        if (userRepository.findByEmail(user.getEmail()) != null){
            model.addAttribute("message", "Пользователь с таким email уже существует");
            return "auth/registration";
        }

        if (!Objects.equals(user.getPassword(), user.getPasswordConfirm())){
            model.addAttribute("message", "Пароли не совпадают");
            return "auth/registration";
        }

        if(bindingResult.hasErrors()){
            return "auth/registration";
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return "redirect:/login";
    }
}
