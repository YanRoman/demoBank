package com.demoBank.controllers;

import com.demoBank.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Objects;

@Controller
public class SettingsController {

    private final UserService userService;
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
    public String changeUsername(String username, Model model,
                                 HttpServletRequest request,
                                 Principal principal){
        if (userService.findByUsername(username) != null){
            model.addAttribute("user", userService.findByUsername(principal.getName()));
            model.addAttribute("message", "Такой пользователь уже существует");
            return "settings";
        }
        if (username.length() < 3){
            model.addAttribute("user", userService.findByUsername(principal.getName()));
            model.addAttribute("message", "Имя должно быть не короче 3 символов");
            return "settings";
        }
        userService.setUsername(username, userService.findByUsername(principal.getName()).getId());
        logout(request);
        return "redirect:/login";
    }

    @PostMapping("/changeEmail")
    public String changeEmail(String email,
                              HttpServletRequest request,
                              Principal principal, Model model){
        if (userService.findByEmail(email) != null){
            model.addAttribute("user", userService.findByUsername(principal.getName()));
            model.addAttribute("message", "Пользователь с таким email уже существует");
            return "settings";
        }
        userService.setEmail(email, userService.findByUsername(principal.getName()).getId());
        logout(request);
        return "redirect:/login";
    }
    @PostMapping("/changeTelephone")
    public String changeTelephone(String telephone,
                              HttpServletRequest request,
                              Principal principal, Model model){
        if (userService.findByTelephone(telephone) != null){
            model.addAttribute("user", userService.findByUsername(principal.getName()));
            model.addAttribute("message", "Пользователь с таким телефоном уже существует");
            return "settings";
        }
        if (telephone.length() != 11){
            model.addAttribute("user", userService.findByUsername(principal.getName()));
            model.addAttribute("message", "Неверный формат телефона");
            return "settings";
        }
        userService.setTelephone(telephone, userService.findByUsername(principal.getName()).getId());
        logout(request);
        return "redirect:/login";
    }

    @PostMapping("/changePassword")
    public String changePassword(String password,
                                 String passwordConfirm,
                                 HttpServletRequest request,
                                 Principal principal,
                                 Model model){
        if (!Objects.equals(password, passwordConfirm)){
            model.addAttribute("user", userService.findByUsername(principal.getName()));
            model.addAttribute("message", "Пароли не совпадают");
            return "settings";
        }
        userService.setPassword(password, userService.findByUsername(principal.getName()).getId());
        logout(request);
        return "redirect:/login";
    }
}
