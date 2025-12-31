package com.Siw.personalProject.service;

import com.Siw.personalProject.model.MessageEntity;
import com.Siw.personalProject.repository.MessageRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 📦 SERVICE LAYER: MessageService
 * ═════════════════════════════════════════════════════════════════════════════════
 * 
 * RESPONSABILITÀ: Gestisce la logica di business per i messaggi di contatto.
 * Implementa operazioni CRUD e funzionalità specializzate (marcare come letto, etc.).
 * 
 * 🎬 CASI D'USO CORRELATI:
 *    - UC2: Invio Messaggio di Contatto (create)
 *    - UC6: Gestione Messaggi Admin (findAll, markAsRead, delete)
 * 
 * 📊 DOMAIN MODEL:
 *    Gestisce il BOUNDED CONTEXT "MESSAGGISTICA":
 *    - Entità: MessageEntity
 *    - Operazioni: CRUD + utility specializzate
 *    - Garanzie: dataInvio sempre impostata, isRead inizializzato
 */
@Service
public class MessageService {

    /**
     * Repository per accedere ai dati dei messaggi dal database.
     * Iniettato automaticamente da Spring via Dependency Injection.
     */
    private final MessageRepository messageRepository;

    /**
     * Costruttore con Dependency Injection.
     * Spring automaticamente fornisce un'istanza di MessageRepository.
     */
    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    /**
     * 🔵 Crea un nuovo messaggio (ricevuto da visitatore anonimo).
     * 
     * 📝 USATO IN:
     *    - HomeController.sendContactMessage() - Quando un visitatore invia il form
     * 
     * 💾 QUERY DATABASE:
     *    INSERT INTO messages (nome, email, message, data_invio, is_read)
     *    VALUES (?, ?, ?, NOW(), false)
     * 
     * ✅ GARANZIE:
     *    - Imposta dataInvio al momento attuale se non presente
     *    - Garantisce isRead = false (nuovo messaggio)
     *    - Non espone il messaggio fino a che l'admin non lo legge
     * 
     * @param message - L'entità MessageEntity da salvare
     * @return L'entità salvata con ID generato dal DB
     * 
     * 🔒 SICUREZZA:\n     *    - Non espone l'email ai template pubblici
     *    - Solo admin può vedere i messaggi
     */
    public MessageEntity create(MessageEntity message) {
        // Garantiamo che dataInvio sia impostata (server-side timestamp)
        if (message.getDataInvio() == null) {
            message.setDataInvio(java.time.LocalDateTime.now());
        }
        message.setRead(false); // Forziamo unread alla creazione
        return messageRepository.save(message);
    }

    /**
     * 🔵 Salva/aggiorna un messaggio esistente.
     * 
     * 📝 USATO IN:
     *    - AdminMessageViewController - Quando l'admin marca come letto/non letto
     * 
     * 💾 QUERY DATABASE:
     *    UPDATE messages SET ... WHERE id = ?
     * 
     * @param message - L'entità MessageEntity da aggiornare
     * @return L'entità aggiornata
     */
    public MessageEntity save(MessageEntity message) {
        return messageRepository.save(message);
    }

    /**
     * 🔵 Restituisce TUTTI i messaggi ordinati per data (nuovi prima).
     * 
     * 📝 USATO IN:
     *    - AdminMessageViewController.listMessages() - Nella lista admin
     * 
     * 💾 QUERY DATABASE:\n     *    SELECT * FROM messages ORDER BY data_invio DESC
     * 
     * @return List di tutti i MessageEntity ordinati cronologicamente (decrescente)
     */
    public List<MessageEntity> findAll() {
        // Usa il metodo derivato per avere l'ordine corretto
        return messageRepository.findAllByOrderByDataInvioDesc();
    }

    /**
     * 🔵 Restituisce SOLO i messaggi non ancora letti.
     * 
     * 📝 USATO IN:\n     *    - AdminDashboardController - Per mostrare "messaggi in sospeso"
     *    - Email di notifica agli admin
     * 
     * 💾 QUERY DATABASE:
     *    SELECT * FROM messages WHERE is_read = false
     * 
     * @return List di MessageEntity con isRead = false
     */
    public List<MessageEntity> findUnread() {
        return messageRepository.findByIsReadFalse();
    }

    /**
     * 🔵 Recupera un messaggio specifico per ID (con validazione).
     * 
     * 📝 USATO IN:
     *    - AdminMessageViewController.viewMessage() - Per mostrare dettagli
     *    - markAsRead() - Per validare prima di marcare
     * 
     * 💾 QUERY DATABASE:
     *    SELECT * FROM messages WHERE id = ?
     * 
     * @param id - L'ID del messaggio da recuperare
     * @return L'entità MessageEntity trovata
     * @throws RuntimeException se il messaggio non esiste
     * 
     * 💡 DIFFERENZA DA findAll():\n     *    - Lancia eccezione se non trovato (non ritorna null)\n     *    - Utile quando siamo certi che il messaggio deve esistere
     */
    public MessageEntity findById(Long id) {
        return messageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Messaggio non trovato con id: " + id));
    }

    /**
     * 🔵 Segna un messaggio come LETTO.
     * 
     * 📝 USATO IN:
     *    - AdminMessageViewController - Quando l'admin clicca su "Leggi"
     * 
     * 💾 QUERY DATABASE (2 step):\n     *    1. SELECT * FROM messages WHERE id = ?\n     *    2. UPDATE messages SET is_read = true WHERE id = ?
     * 
     * @param id - L'ID del messaggio da marcare come letto
     * @return Il MessageEntity aggiornato con isRead = true
     * @throws RuntimeException se il messaggio non esiste
     */
    public MessageEntity markAsRead(Long id) {
        MessageEntity msg = findById(id); // Può lanciare eccezione
        msg.setRead(true);
        return messageRepository.save(msg);
    }

    /**
     * 🔵 Elimina un messaggio permanentemente.
     * 
     * 📝 USATO IN:
     *    - AdminMessageViewController - Quando l'admin clicca "Elimina"
     * 
     * 💾 QUERY DATABASE:
     *    DELETE FROM messages WHERE id = ?
     * 
     * @param id - L'ID del messaggio da eliminare
     * @throws RuntimeException se il messaggio non esiste
     * 
     * ⚠️ ATTENZIONE:\n     *    - OPERAZIONE PERMANENTE, non recuperabile\n     *    - Considerate di usare un soft-delete (campo deleted_at)\n     *    - Oppure mantenere un backup/archivio
     */
    public void delete(Long id) {
        // Verifichiamo prima l'esistenza per feedback migliore all'utente
        if (!messageRepository.existsById(id)) {
            throw new RuntimeException("Messaggio non trovato con id: " + id);
        }
        messageRepository.deleteById(id);
    }
}
