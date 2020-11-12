package com.smeshariks.pms.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("/home")
    public String homePage(Model model) {
        return "home";
    }

    @GetMapping("/main")
    public String mainPage(Model model) {
        return "main";
    }
}

