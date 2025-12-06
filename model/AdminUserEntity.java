package com.Siw.personalProject.model;

import jakarta.persistence.*;

@Entity
@Table(name = "admin_users")
public class AdminUserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password; // ricordati di memorizzare sempre l'hash, mai la password in chiaro

    // ----- COSTRUTTORI -----
    public AdminUserEntity() {}

    public AdminUserEntity(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // ----- GETTER & SETTER -----
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
