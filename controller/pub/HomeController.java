package com.Siw.personalProject.controller.pub;

import com.Siw.personalProject.model.MessageEntity;
import com.Siw.personalProject.request.MessageRequest;
import com.Siw.personalProject.service.ExperienceService;
import com.Siw.personalProject.service.MessageService;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Controller
public class HomeController {

    private final MessageService messageService;
    private final ExperienceService experienceService; // <- aggiungi

    public HomeController(MessageService messageService, ExperienceService experienceService) {
        this.messageService = messageService;
        this.experienceService = experienceService; // <- inizializza
    }

    @GetMapping({"/", "/index"})
    public String home(Model model) {
        if (!model.containsAttribute("messageRequest")) {
            model.addAttribute("messageRequest", new MessageRequest());
        }

        // Usa il bean, non la classe statica
        model.addAttribute("experiences", experienceService.findAll());

        return "index";
    }

    @GetMapping("/contact")
    public String contactForm(Model model) {
        model.addAttribute("messageRequest", new MessageRequest());
        return "contact";
    }

    @PostMapping("/contact")
    public String sendContactMessage(@Valid @ModelAttribute("messageRequest") MessageRequest request, Model model) {
        MessageEntity message = new MessageEntity();
        message.setNome(request.getNome());
        message.setEmail(request.getEmail());
        message.setMessage(request.getMessage());
        message.setDataInvio(LocalDateTime.now());
        message.setRead(false);

        messageService.create(message);

        model.addAttribute("success", true);
        model.addAttribute("messageRequest", new MessageRequest());

        return "contact";
    }
}

