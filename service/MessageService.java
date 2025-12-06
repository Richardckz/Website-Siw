package com.Siw.personalProject.service;

import com.Siw.personalProject.model.MessageEntity;
import com.Siw.personalProject.repository.MessageRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService {

    private final MessageRepository messageRepository;

    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    // 🔵 Crea un nuovo messaggio (usato dall'API pubblica)
    public MessageEntity create(MessageEntity message) {
        // garantiamo che dataInvio sia impostata e isRead sia false per default
        if (message.getDataInvio() == null) {
            message.setDataInvio(java.time.LocalDateTime.now());
        }
        message.setRead(false); // forziamo unread alla creazione
        return messageRepository.save(message);
    }

    // 🔵 Salva/aggiorna un'entità (usato dal controller admin per marcare read/unread)
    public MessageEntity save(MessageEntity message) {
        return messageRepository.save(message);
    }

    // 🔵 Restituisce tutti i messaggi ordinati per data (nuovi prima)
    public List<MessageEntity> findAll() {
        // usa il metodo derivato per avere l'ordine corretto
        return messageRepository.findAllByOrderByDataInvioDesc();
    }

    // 🔵 Restituisce solo i messaggi non letti
    public List<MessageEntity> findUnread() {
        return messageRepository.findByIsReadFalse();
    }

    // 🔵 Recupera per id (lancia eccezione se non trovato)
    public MessageEntity findById(Long id) {
        return messageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Messaggio non trovato con id: " + id));
    }

    // 🔵 Segna un messaggio come letto (utility)
    public MessageEntity markAsRead(Long id) {
        MessageEntity msg = findById(id);
        msg.setRead(true);
        return messageRepository.save(msg);
    }

    // 🔵 Elimina un messaggio
    public void delete(Long id) {
        // opzionalmente: verificare prima l'esistenza
        if (!messageRepository.existsById(id)) {
            throw new RuntimeException("Messaggio non trovato con id: " + id);
        }
        messageRepository.deleteById(id);
    }
}
