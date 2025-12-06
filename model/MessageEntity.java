package com.Siw.personalProject.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "messages")
public class MessageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    private String email;

    @Column(columnDefinition = "TEXT")
    private String message;

    private LocalDateTime dataInvio;

    // campo primitivo: default false
    private boolean isRead;

    // ----- COSTRUTTORI -----
    public MessageEntity() {}

    // ----- GETTER & SETTER -----
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public LocalDateTime getDataInvio() { return dataInvio; }
    public void setDataInvio(LocalDateTime dataInvio) { this.dataInvio = dataInvio; }

    public boolean isRead() { return isRead; }
    public void setRead(boolean read) { isRead = read; }
}
