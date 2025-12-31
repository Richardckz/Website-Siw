package com.Siw.personalProject.controller.admin;

import com.Siw.personalProject.service.ExperienceService;
import com.Siw.personalProject.service.MessageService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminDashboardController {

    private final ExperienceService experienceService;
    private final MessageService messageService;

    public AdminDashboardController(ExperienceService experienceService, MessageService messageService) {
        this.experienceService = experienceService;
        this.messageService = messageService;
    }

    // Redirect da /admin a /admin/dashboard
    @GetMapping
    public String redirectToDashboard() {
        return "redirect:/admin/dashboard";
    }

    // Pagina principale dashboard
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        // Statistiche esperienze
        long totalExperiences = experienceService.findAll().size();
        
        // Statistiche messaggi
        long totalMessages = messageService.findAll().size();
        long unreadMessages = messageService.findAll().stream()
                .filter(msg -> !msg.isRead())
                .count();

        // Aggiungi statistiche al model
        model.addAttribute("totalExperiences", totalExperiences);
        model.addAttribute("totalMessages", totalMessages);
        model.addAttribute("unreadMessages", unreadMessages);

        // Link alle sezioni
        model.addAttribute("experienceUrl", "/admin/experience");
        model.addAttribute("messagesUrl", "/admin/messages");

        return "admin/dashboard";
    }
}
