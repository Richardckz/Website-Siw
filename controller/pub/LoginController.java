package com.Siw.personalProject.controller.pub;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    // Mappa la route /login
    @GetMapping("/login")
    public String login() {
        // Ritorna il nome del template Thymeleaf senza estensione
        return "login"; // Spring cerca login.html in src/main/resources/templates/
    }
}
