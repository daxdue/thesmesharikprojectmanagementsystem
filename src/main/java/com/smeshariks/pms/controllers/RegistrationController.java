package com.smeshariks.pms.controllers;

import com.smeshariks.pms.entities.SmesharikCredentials;
import com.smeshariks.pms.entities.User;
import com.smeshariks.pms.services.SmesharikCredentialsService;
import com.smeshariks.pms.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
public class RegistrationController {

    @Autowired
    private UserService userService;

    @GetMapping("/signup")
    public String signup(Model model) {
        model.addAttribute("userForm", new User());
        return "signup";
    }
    @PostMapping("/signup")
    public String addUser(@ModelAttribute("userForm") @Valid User userForm, BindingResult
                                          bindingResult, Model model) {
        System.out.println("Request!");
        /*
        if(bindingResult.hasErrors()) {
            return "signup";
        }
        */

        if(!userService.saveUser(userForm)) {
            model.addAttribute("usernameError", "Пользователь с таким именем уже существует");
            return "signup";
        }

        return "redirect:/";
    }
}
