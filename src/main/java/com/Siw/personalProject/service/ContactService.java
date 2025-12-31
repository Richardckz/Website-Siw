package com.Siw.personalProject.service;

import com.Siw.personalProject.request.MessageRequest;
import com.Siw.personalProject.repository.MessageRepository;
import org.springframework.stereotype.Service;

@Service
public class ContactService {

    private final MessageRepository messageRepository;

    public ContactService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public void saveMessage(MessageRequest messageRequest) {
        // Converti MessageRequest in MessageEntity e salva
        messageRepository.save(messageRequest.toEntity());
    }
}
