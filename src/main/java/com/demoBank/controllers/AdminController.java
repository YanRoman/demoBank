package com.demoBank.controllers;

import com.demoBank.entities.User;
import com.demoBank.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminController {
    private final UserService userService;
    @Autowired
    public AdminController(UserService userService){
        this.userService = userService;
    }

    @GetMapping("/admin")
    public String userList(Model model) {
        model.addAttribute("users", userService.allUsers());
        return "admin";
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") Long id, Model model) {

        userService.deleteUser(id);
        return "redirect:/admin";
    }
}
