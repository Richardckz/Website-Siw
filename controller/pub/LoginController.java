package com.Siw.personalProject.controller.pub;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    // Mappa la route /login
    @GetMapping("/login")
    public String login(@RequestParam(value = "redirect", required = false) String redirect, Model model) {
        // Pass the optional redirect param to the template so the form can forward it on submit
        model.addAttribute("redirect", redirect);
        return "login"; // Spring cerca login.html in src/main/resources/templates/
    }
} 