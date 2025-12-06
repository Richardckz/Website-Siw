package com.Siw.personalProject.controller.pub;

import com.Siw.personalProject.model.MessageEntity;
import com.Siw.personalProject.request.MessageRequest;
import com.Siw.personalProject.service.MessageService;
import com.Siw.personalProject.dto.MessageDTO;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/contact")
public class PublicContactController {

    private final MessageService messageService;

    public PublicContactController(MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping
    public MessageDTO sendMessage(@Valid @RequestBody MessageRequest request) {

        MessageEntity message = new MessageEntity();
        message.setNome(request.getNome());
        message.setEmail(request.getEmail());
        message.setMessage(request.getMessage());
        message.setDataInvio(LocalDateTime.now());
        message.setRead(false);

        MessageEntity saved = messageService.create(message);

        return MessageDTO.fromEntity(saved);
    }
}
