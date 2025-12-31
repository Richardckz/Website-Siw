package com.Siw.personalProject.controller.admin;

import com.Siw.personalProject.model.MessageEntity;
import com.Siw.personalProject.service.MessageService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin/messages")
public class AdminMessageViewController {

    private final MessageService messageService;

    public AdminMessageViewController(MessageService messageService) {
        this.messageService = messageService;
    }

    // LISTA COMPLETA MESSAGGI
    @GetMapping
    public String list(Model model) {
        List<MessageEntity> messages = messageService.findAll();
        model.addAttribute("messages", messages);
        return "admin/messages/list";  // templates/admin/messages/list.html
    }

    // VISUALIZZA UN MESSAGGIO SPECIFICO
    @GetMapping("/{id}")
    public String view(@PathVariable Long id, Model model) {
        MessageEntity message = messageService.findById(id);

        // segna come letto quando viene aperto
        if (!message.isRead()) {
            message.setRead(true);
            messageService.save(message);
        }

        model.addAttribute("message", message);
        return "admin/messages/view";  // templates/admin/messages/view.html
    }

    // MARCA COME NON LETTO
    @GetMapping("/unread/{id}")
    public String markUnread(@PathVariable Long id) {
        MessageEntity message = messageService.findById(id);
        message.setRead(false);
        messageService.save(message);

        return "redirect:/admin/messages/" + id;
    }

    // CANCELLA UN MESSAGGIO
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        messageService.delete(id);
        return "redirect:/admin/messages";
    }
}