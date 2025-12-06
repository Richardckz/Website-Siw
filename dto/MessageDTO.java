package com.Siw.personalProject.dto;

import com.Siw.personalProject.model.MessageEntity;
import java.time.LocalDateTime;

public class MessageDTO {
    private Long id;
    private String nome;
    private String email;
    private String message;
    private LocalDateTime dataInvio;

    public MessageDTO() {}

    public MessageDTO(Long id, String nome, String email, String message, LocalDateTime dataInvio) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.message = message;
        this.dataInvio = dataInvio;
    }

    // ----- Metodo statico per convertire da entity -----
    public static MessageDTO fromEntity(MessageEntity entity) {
        if (entity == null) return null;
        return new MessageDTO(
            entity.getId(),
            entity.getNome(),
            entity.getEmail(),
            entity.getMessage(),
            entity.getDataInvio()
        );
    }

    // Getter & Setter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public java.time.LocalDateTime getDataInvio() { return dataInvio; }
    public void setDataInvio(java.time.LocalDateTime dataInvio) { this.dataInvio = dataInvio; }
}
