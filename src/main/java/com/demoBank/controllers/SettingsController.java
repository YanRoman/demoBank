package com.demoBank.controllers;

import com.demoBank.entities.User;
import com.demoBank.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.HttpServerErrorException;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Objects;

@Controller
public class SettingsController {

    private UserService userService;
    @Autowired
    public SettingsController(UserService userService){
        this.userService = userService;
    }

    @GetMapping("/settings")
    public String change(Model model, Principal principal){
        model.addAttribute("user", userService.findByUsername(principal.getName()));
        return "settings";
    }

    private void logout(HttpServletRequest request){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null){
            request.getSession().invalidate();
        }
    }
    @PostMapping("/changeUsername")
    public String changeUsername(@ModelAttribute("username") String username,
                                 HttpServletRequest request,
                                 Principal principal){
        userService.setUsername(username, principal);
        logout(request);
        return "redirect:/login";
    }

    @PostMapping("/changeEmail")
    public String changeEmail(@ModelAttribute("email") String email,
                              HttpServletRequest request,
                              Principal principal){
        userService.setEmail(email, principal);
        logout(request);
        return "redirect:/login";
    }

    @PostMapping("/changePassword")
    public String changePassword(@ModelAttribute("password") String password,
                                 @ModelAttribute("passwordConfirm") String passwordConfirm,
                                 HttpServletRequest request,
                                 Principal principal,
                                 Model model){
        if (!Objects.equals(password, passwordConfirm)){
            model.addAttribute("user", userService.findByUsername(principal.getName()));
            model.addAttribute("message", "Пароли не совпадают");
            return "settings";
        }
        userService.setPassword(password, principal);
        logout(request);
        return "redirect:/login";
    }
}
