package com.Siw.personalProject.model;

import jakarta.persistence.*;

/**
 * 🏷️ ENTITÀ DOMINIO: AdminUserEntity
 * ═════════════════════════════════════════════════════════════════════════════════
 * 
 * Rappresenta un utente amministratore del sistema di portfolio.
 * Questa entità è persisted nel database e gestisce le credenziali di accesso.
 * 
 * 📊 DOMAIN MODEL:
 *    - AdminUserEntity appartiene al bounded context "AUTHENTICATION"
 *    - Responsabilità: Archiviare credenziali di admin per il login
 *    - Ciclo di vita: Creata dall'admin, usata per validazione login
 * 
 * 🎬 CASI D'USO COINVOLTI:
 *    - UC3: Login Amministratore
 *    - UC4: Visualizzazione Dashboard Admin
 *    - UC7: Logout
 * 
 * ⚠️ SICUREZZA CRITICA:
 *    La password deve SEMPRE essere hashata con BCrypt (vedi SecurityConfig).
 *    Non memorizzare MAI password in chiaro nel database!
 */
@Entity
@Table(name = "admin_users")
public class AdminUserEntity {

    /**
     * ID univoco dell'amministratore.
     * AUTO-GENERATED dal database (IDENTITY strategy).
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Username univoco per il login.
     * UNIQUE constraint: non possono esserci due admin con lo stesso username.
     * NOT NULL: obbligatorio per l'autenticazione.
     */
    @Column(unique = true, nullable = false)
    private String username;

    /**
     * Password hashata con BCrypt.
     * ⚠️ IMPORTANTE:
     *    - Memorizza l'HASH della password (es: $2a$10$...)
     *    - Non memorizzare mai la password in chiaro
     *    - Hashing gestito da BCryptPasswordEncoder in SecurityConfig
     * 
     * FLUSSO DI SICUREZZA:
     *    1. Utente inserisce password nel form di login
     *    2. Spring Security passa a BCryptPasswordEncoder.matches()
     *    3. Compara il plaintext con l'hash memorizzato
     *    4. Se valido, crea la sessione autenticata
     */
    @Column(nullable = false)
    private String password;

    // ═════════════════════════════════════════════════════════════════════════════════
    // COSTRUTTORI
    // ═════════════════════════════════════════════════════════════════════════════════
    
    /**
     * Costruttore default (richiesto da JPA/Hibernate).
     * Usato internamente dal framework quando carica gli oggetti dal DB.
     */
    public AdminUserEntity() {}

    /**
     * Costruttore di convenienza per la creazione di nuovi admin.
     * 
     * UTILIZZO:
     *    AdminUserEntity admin = new AdminUserEntity("mario", "$2a$10$...hash...");
     *    adminUserService.create(admin);
     * 
     * @param username - Lo username univoco per il login (es: "mario")
     * @param password - La password HASHATA con BCrypt (es: "$2a$10$...")
     */
    public AdminUserEntity(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // ═════════════════════════════════════════════════════════════════════════════════
    // GETTER & SETTER (Accesso agli attributi)
    // ═════════════════════════════════════════════════════════════════════════════════
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    /**
     * Ottiene lo username dell'admin.
     * Usato durante il login per identificare l'utente.
     */
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    /**
     * Ottiene la password hashata.
     * ⚠️ Non esporre mai il getter di password ai template/API!
     * Usato SOLO internamente da Spring Security per la validazione.
     */
    public String getPassword() { return password; }
    
    /**
     * Imposta la password (deve essere PRE-HASHATA con BCrypt!).
     * Non deve mai ricevere una password in chiaro.
     * 
     * CORRETTO:
     *    String hashedPassword = passwordEncoder.encode(rawPassword);
     *    admin.setPassword(hashedPassword);
     * 
     * @param password - Hash BCrypt della password (non plaintext!)
     */
    public void setPassword(String password) { this.password = password; }
}

