package com.smeshariks.pms.controllers;

import com.smeshariks.pms.entities.User;
import com.smeshariks.pms.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
public class RegistrationController {

    @Autowired
    private UserService userService;

    @GetMapping("/signup")
    public String signup(Model model) {
        model.addAttribute("userForm", new User());
        return "registration";
    }
    @PostMapping("/signup")
    public String addUser(@ModelAttribute("userForm") @Valid User userForm, BindingResult
                                          bindingResult, Model model) {
        /*
        if(bindingResult.hasErrors()) {
            return "signup";
        }
        */

        if(!userService.saveUser(userForm)) {
            model.addAttribute("usernameError", true);
            return "registration";
        }

        return "redirect:/login";
    }

    @RequestMapping("/login-error")
    public String loginError(Model model) {
        //User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        //model.addAttribute("user", user);
        model.addAttribute("loginError", true);
        return "login";
    }
}
