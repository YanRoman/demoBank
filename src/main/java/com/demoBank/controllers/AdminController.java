package com.demoBank.controllers;

import com.demoBank.entities.User;
import com.demoBank.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Objects;
import java.util.Optional;

@Controller
@PreAuthorize("hasAuthority('ADMIN')")
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;
    @Autowired
    public AdminController(UserService userService){
        this.userService = userService;
    }

    @GetMapping()
    public String userList(Model model) {
        model.addAttribute("users", userService.allUsers());
        return "admin";
    }
    @GetMapping("/addUser")
    public String addUser(Model model){
        model.addAttribute("user", new User());
        return "addUser";
    }
    @PostMapping("/addUser")
    public String addUser(@Valid User user, BindingResult bindingResult, Model model){
        if(bindingResult.hasErrors()){
            model.addAttribute("user", user);
            return "addUser";
        }
        if (user.getPassword() != null && !Objects.equals(user.getPassword(), user.getPasswordConfirm())){
            model.addAttribute("message", "Пароли не совпадают");
            return "addUser";
        }
        if (userService.findByEmail(user.getEmail()) != null){
            model.addAttribute("message", "Пользователь с таким email уже существует");
            return "addUser";
        }
        if (!userService.saveUser(user)){
            model.addAttribute("message", "Пользователь с таким именем уже существует");
            return "auth/registration";
        }
        return "redirect:/admin";
    }


    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") Long id) {
        userService.deleteById(id);
        return "redirect:/admin";
    }

    @GetMapping("/update/{id}")
    public String updateUser(@PathVariable Long id, Model model){
        model.addAttribute("user", userService.findById(id).get());
        return "adminUpdate";
    }

    @PostMapping("/admChangeUsername/{id}")
    public String admChangeUsername(@PathVariable Long id, String username, Model model, Principal principal){

        System.out.println(id);
        if (username.length() < 3){
            model.addAttribute("user", userService.findByUsername(principal.getName()));
            model.addAttribute("message", "Имя должно быть не короче 3 символов");
            return "adminUpdate";
        }
        if (!userService.setUsername(username, id)){
            model.addAttribute("user", userService.findByUsername(principal.getName()));
            model.addAttribute("message", "Такой пользователь уже существует");
            return "adminUpdate";
        }
        return "redirect:/admin";
    }

    @PostMapping("/admChangeEmail/{id}")
    public String admChangeEmail(@PathVariable Long id, String email, Model model, Principal principal){
        if (!userService.setEmail(email, userService.findByUsername(principal.getName()).getId())){
            model.addAttribute("user", userService.findByUsername(principal.getName()));
            model.addAttribute("message", "Пользователь с таким email уже существует");
            return "adminUpdate";
        }
        return "redirect:/admin";
    }

    @PostMapping("/admChangeTelephone/{id}")
    public String admChangeTelephone(@PathVariable Long id, String telephone, Model model, Principal principal){
        if (telephone.length() != 11){
            model.addAttribute("user", userService.findByUsername(principal.getName()));
            model.addAttribute("message", "Неверный формат телефона");
            return "adminUpdate";
        }
        if (!userService.setTelephone(telephone, userService.findByUsername(principal.getName()).getId())){
            model.addAttribute("user", userService.findByUsername(principal.getName()));
            model.addAttribute("message", "Пользователь с таким телефоном уже существует");
            return "adminUpdate";
        }

        return "redirect:/admin";
    }

    @PostMapping("/admChangePassword/{id}")
    public String admChangePassword(@PathVariable Long id, String password, String passwordConfirm,
                                    Model model, Principal principal){
        if (!Objects.equals(password, passwordConfirm)){
            model.addAttribute("user", userService.findByUsername(principal.getName()));
            model.addAttribute("message", "Пароли не совпадают");
            return "adminUpdate";
        }
        userService.setPassword(password, userService.findByUsername(principal.getName()).getId());
        return "redirect:/admin";
    }
}
