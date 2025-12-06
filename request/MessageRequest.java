package com.Siw.personalProject.request;

import com.Siw.personalProject.model.MessageEntity;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

public class MessageRequest {

    @NotBlank(message = "Il nome è obbligatorio")
    private String nome;

    @NotBlank(message = "L'email è obbligatoria")
    @Email(message = "Email non valida")
    private String email;

    @NotBlank(message = "Il messaggio non può essere vuoto")
    @Size(max = 3000, message = "Il messaggio può contenere al massimo 3000 caratteri")
    private String message;

    // Getter & Setter
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    // 🔹 Metodo per convertire il form in entity
    public MessageEntity toEntity() {
        MessageEntity entity = new MessageEntity();
        entity.setNome(this.nome);
        entity.setEmail(this.email);
        entity.setMessage(this.message);
        entity.setDataInvio(LocalDateTime.now());
        // isRead di default sarà false
        return entity;
    }
}
