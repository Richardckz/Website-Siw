# 📋 Analisi Completa del Progetto: Personal Portfolio

## 📌 Panoramica del Progetto

Questo è un'applicazione **Spring Boot 4.0.1** che implementa un **Portfolio Personale** con funzionalità di gestione contenuti e messaggistica. L'applicazione è costruita con un'architettura MVC (Model-View-Controller) e utilizza **Java 21 LTS** come runtime.

**Stack Tecnologico:**
- **Backend:** Spring Boot 4.0.1, Spring Data JPA, Spring Security
- **Frontend:** Thymeleaf, Bootstrap, jQuery
- **Database:** PostgreSQL (produzione), H2 (test)
- **Build:** Maven
- **Runtime:** Java 21 LTS
- **Server:** Tomcat integrato in Spring Boot

---

## 🗂️ Architettura del Progetto

```
src/
├── main/
│   ├── java/com/Siw/personalProject/
│   │   ├── controller/          # Gestione delle richieste HTTP
│   │   ├── service/             # Logica di business
│   │   ├── model/               # Entità JPA (Domain Model)
│   │   ├── repository/          # Accesso ai dati (Data Access Layer)
│   │   ├── request/             # DTO per le richieste
│   │   ├── config/              # Configurazione Spring
│   │   └── PersonalProjectApplication.java  # Entry point
│   └── resources/
│       ├── templates/           # Template Thymeleaf (HTML)
│       ├── static/              # File statici (CSS, JS, immagini)
│       └── application.properties  # Configurazione
└── test/
    └── java/                    # Test unitari e integrazione
```

---

## 🎯 Modello di Dominio (Domain Model)

Il dominio dell'applicazione è composto da **3 entità principali**:

### 1️⃣ **AdminUserEntity** 
Rappresenta gli utenti amministratori del sistema.

**Responsabilità:**
- Autenticazione e autorizzazione
- Gestione delle credenziali di accesso

**Attributi:**
| Campo | Tipo | Descrizione |
|-------|------|-------------|
| `id` | Long (PK) | Identificativo univoco |
| `username` | String (UNIQUE) | Nome utente per il login |
| `password` | String | Hash della password (BCrypt) ⚠️ *mai in chiaro* |

**Relazioni:** Nessuna (entità indipendente)

---

### 2️⃣ **ExperienceEntity** 
Rappresenta le esperienze lavorative/professionali.

**Responsabilità:**
- Archiviare i dati delle esperienze di lavoro
- Visualizzazione nel portfolio pubblico

**Attributi:**
| Campo | Tipo | Descrizione |
|-------|------|-------------|
| `id` | Long (PK) | Identificativo univoco |
| `role` | String | Posizione ricoperta (es. "Senior Developer") |
| `company` | String | Nome azienda |
| `startDate` | LocalDate | Data inizio esperienza |
| `endDate` | LocalDate | Data fine esperienza (null se ancora attivo) |
| `description` | String | Descrizione dettagliata dell'esperienza |

**Relazioni:** Nessuna (entità indipendente)

---

### 3️⃣ **MessageEntity** 
Rappresenta i messaggi ricevuti dai visitatori tramite il form di contatto.

**Responsabilità:**
- Archiviare i messaggi di contatto
- Tracciamento dello stato di lettura

**Attributi:**
| Campo | Tipo | Descrizione |
|-------|------|-------------|
| `id` | Long (PK) | Identificativo univoco |
| `nome` | String | Nome del mittente |
| `email` | String | Email del mittente |
| `message` | String | Contenuto del messaggio (TEXT) |
| `dataInvio` | LocalDateTime | Timestamp di invio |
| `isRead` | boolean | Flag di lettura (default: false) |

**Relazioni:** Nessuna (entità indipendente)

---

### 📊 Diagramma Entità-Relazioni (ER)

```
┌──────────────────────┐
│   AdminUserEntity    │
├──────────────────────┤
│ PK: id               │
│ username (UNIQUE)    │
│ password (BCrypt)    │
└──────────────────────┘

┌──────────────────────┐
│  ExperienceEntity    │
├──────────────────────┤
│ PK: id               │
│ role                 │
│ company              │
│ startDate            │
│ endDate              │
│ description          │
└──────────────────────┘

┌──────────────────────┐
│   MessageEntity      │
├──────────────────────┤
│ PK: id               │
│ nome                 │
│ email                │
│ message              │
│ dataInvio            │
│ isRead               │
└──────────────────────┘

⚠️ NOTA: Non ci sono relazioni dirette tra le entità (no FK)
    Ogni entità è indipendente e gestisce dati specifici.
```

---

## 🎬 Casi d'Uso (Use Cases)

### **UC1: Visualizzazione Portfolio Pubblico** 
**Attore:** Visitatore anonimo  
**Precondizioni:** Nessuna  
**Flusso principale:**
1. L'utente accede alla homepage (`GET /` o `/index`)
2. Il sistema carica tutte le esperienze dal database tramite `ExperienceService`
3. Thymeleaf renderizza le esperienze nella pagina HTML
4. L'utente vede il portfolio con tutte le esperienze elencate

**Postcondizioni:** Portfolio visualizzato correttamente  
**Codice di riferimento:** `HomeController.home()`

---

### **UC2: Invio Messaggio di Contatto**
**Attore:** Visitatore anonimo  
**Precondizioni:** Visitatore è sulla pagina di contatto  
**Flusso principale:**
1. Visitatore riempie il form di contatto con nome, email, messaggio
2. Invia il modulo (`POST /contact`)
3. `HomeController` valida il `MessageRequest` (@Valid)
4. Se valido, crea una nuova `MessageEntity` con timestamp attuale
5. `MessageService` salva il messaggio nel database
6. Sistema ritorna la pagina di contatto con messaggio di successo

**Postcondizioni:** Messaggio archiviato nel DB, email admin notificato  
**Codice di riferimento:** `HomeController.sendContactMessage()`

---

### **UC3: Login Amministratore**
**Attore:** Amministratore  
**Precondizioni:** Admin esiste nel database con password hashata  
**Flusso principale:**
1. Admin accede a `/login`
2. Inserisce username e password nel form di login
3. Spring Security valida le credenziali tramite `AdminUserService`
4. Password viene confrontata con BCrypt hash nel DB
5. Se valida, Spring Security crea una sessione AUTHENTICATED con role ROLE_ADMIN
6. Redirect a `/admin/dashboard`

**Postcondizioni:** Admin autenticato, sessione attiva  
**Codice di riferimento:** `SecurityConfig`, `AdminUserService.findByUsername()`

---

### **UC4: Visualizzazione Dashboard Admin**
**Attore:** Amministratore (autenticato con ROLE_ADMIN)  
**Precondizioni:** Admin loggato (session attiva)  
**Flusso principale:**
1. Admin accede a `/admin/dashboard`
2. Spring Security verifica che l'utente ha role ROLE_ADMIN (vedi `SecurityConfig` - `@RequestMatchers("/admin/**").hasRole("ADMIN")`)
3. `AdminDashboardController` carica dati riepilogativi
4. Dashboard mostra opzioni di gestione esperienze e messaggi

**Postcondizioni:** Dashboard visualizzata, opzioni amministrative disponibili  
**Codice di riferimento:** `AdminDashboardController`

---

### **UC5: Gestione Esperienze (CRUD)**
**Attore:** Amministratore  
**Precondizioni:** Admin loggato

#### **UC5a: Creazione Esperienza**
1. Admin accede al form di creazione (`/admin/experiences/create`)
2. Compila i campi: role, company, startDate, endDate, description
3. Invia il form (`POST /admin/experiences`)
4. `AdminExperienceController` valida e crea `ExperienceEntity`
5. `ExperienceService` salva nel database
6. Redirect alla lista esperienze

#### **UC5b: Visualizzazione Lista**
1. Admin accede a `/admin/experiences`
2. `ExperienceService.findAll()` recupera tutte le esperienze
3. Lista viene renderizzata in template

#### **UC5c: Modifica**
1. Admin seleziona un'esperienza e accede al form di modifica
2. Aggiorna i campi
3. `ExperienceService.update()` salva le modifiche

#### **UC5d: Eliminazione**
1. Admin seleziona un'esperienza
2. Clicca su "Elimina"
3. `ExperienceService.delete()` rimuove dal database

**Codice di riferimento:** `AdminExperienceController`, `ExperienceService`

---

### **UC6: Gestione Messaggi**
**Attore:** Amministratore  
**Precondizioni:** Admin loggato  
**Flusso principale:**
1. Admin accede a `/admin/messages`
2. `AdminMessageViewController` carica tutti i messaggi
3. Mostra lista messaggi con colonna "Letto/Non Letto"
4. Admin può cliccare su un messaggio per leggere il dettaglio
5. Al clic, `MessageEntity.isRead` viene aggiornato a `true`
6. Admin vede il messaggio completo

**Postcondizioni:** Messaggi visualizzati e marcati come letti  
**Codice di riferimento:** `AdminMessageViewController`, `MessageService`

---

### **UC7: Logout**
**Attore:** Amministratore  
**Precondizioni:** Admin loggato  
**Flusso principale:**
1. Admin clicca su "Logout" o accede a `/logout`
2. Spring Security invalida la sessione
3. Cookie JSESSIONID rimosso
4. Redirect a homepage (`/`)

**Postcondizioni:** Admin sloggato, sessione terminata  
**Codice di riferimento:** `SecurityConfig.logout()`

---

## 🔐 Architettura di Sicurezza

```
                    ┌─────────────────────┐
                    │  Visitatore/Admin   │
                    └──────────┬──────────┘
                               │ HTTP Request
                               ▼
                    ┌─────────────────────┐
                    │ Spring Security     │
                    │ FilterChain         │
                    ├─────────────────────┤
                    │ • CSRF Protection   │
                    │ • Auth Check        │
                    │ • Role Authorization│
                    └──────────┬──────────┘
                               │
                    ┌──────────┴──────────┐
                    ▼                     ▼
          Permesso ✅             Negato ❌
                    │                     │
                    ▼                     ▼
              Controller          403 Forbidden
            (HomeController,
          AdminDashboard...)
```

**Regole di Autorizzazione:**

| Rotta | Permesso | Nota |
|-------|----------|------|
| `/`, `/index`, `/contact` | PUBLIC | Accessibile a tutti |
| `/css/**`, `/js/**`, `/img/**`, `/libraries/**` | PUBLIC | Risorse statiche |
| `/admin/**` | Solo ROLE_ADMIN | Richiede autenticazione e ruolo ADMIN |
| `/login` | PUBLIC | Accessibile a tutti per autenticarsi |
| Altre rotte | AUTHENTICATED | Richiede autenticazione |

---

## 📂 Stack della Presentazione (Templates Thymeleaf)

```
templates/
├── index.html          # Homepage con portfolio
│   ├── Elenca ExperienceEntity
│   └── Form MessageRequest per contatti
├── contact.html        # Pagina contatti
│   └── Form MessageRequest
├── login.html          # Form login
│   └── Form di login con username/password
└── admin/
    ├── dashboard.html  # Dashboard amministrativa
    ├── experience/
    │   ├── list.html   # Lista esperienze
    │   └── form.html   # Form creazione/modifica
    └── messages/
        ├── list.html   # Lista messaggi
        └── view.html   # Visualizzazione dettaglio
```

---

## 🛠️ Stack della Logica di Business (Services)

### **ExperienceService**
```java
// Responsabilità: Gestione logica per le esperienze
- findAll()          → Recupera tutte le esperienze
- findById(id)       → Recupera un'esperienza per ID
- create(entity)     → Crea una nuova esperienza
- update(entity)     → Aggiorna un'esperienza esistente
- delete(id)         → Elimina un'esperienza
```

### **MessageService**
```java
// Responsabilità: Gestione logica per i messaggi
- create(message)    → Salva un nuovo messaggio
- findAll()          → Recupera tutti i messaggi
- findById(id)       → Recupera un messaggio per ID
- markAsRead(id)     → Marca un messaggio come letto
- delete(id)         → Elimina un messaggio
```

### **AdminUserService**
```java
// Responsabilità: Gestione utenti amministratori
- findByUsername(username)  → Trova admin per username (per login)
- create(user)              → Crea nuovo admin
- usernameExists(username)  → Verifica disponibilità username
```

### **ContactService**
```java
// Responsabilità: Gestione comunicazioni di contatto
- (Metodi specifici del dominio)
```

---

## 🎯 Flusso Richiesta-Risposta (Request-Response Cycle)

### **Esempio: Invio Messaggio di Contatto**

```
1. Visitatore riempie form contatti
   └─ Nome: "Mario Rossi"
   └─ Email: "mario@example.com"
   └─ Messaggio: "Interessato ai vostri servizi"

2. POST /contact
   └─ HomeController.sendContactMessage() riceve la richiesta
   └─ @Valid valida MessageRequest

3. Creazione MessageEntity
   ├─ nome = "Mario Rossi"
   ├─ email = "mario@example.com"
   ├─ message = "Interessato ai vostri servizi"
   ├─ dataInvio = LocalDateTime.now()
   └─ isRead = false

4. MessageService.create(message)
   └─ messageRepository.save(message)
   └─ INSERT INTO messages (nome, email, message, data_invio, is_read)

5. Database Response
   └─ MessageEntity salvato con ID generato

6. HTTP Response 200 OK
   └─ Template contact.html con model.addAttribute("success", true)
   └─ Visitatore vede: "Messaggio inviato con successo!"
```

---

## 🔑 Concetti Chiave dell'Architettura

### **1. Dependency Injection (DI)**
Tutte le dipendenze sono iniettate via costruttore:

```java
// ✅ BUONA PRATICA (HomeController.java)
public HomeController(MessageService messageService, ExperienceService experienceService) {
    this.messageService = messageService;
    this.experienceService = experienceService;
}

// ❌ EVITARE: Accesso statico alle classi
// ExperienceEntity.findAll()  // ← SBAGLIATO
```

### **2. Validazione con Jakarta Validation**
```java
@Valid @ModelAttribute("messageRequest") MessageRequest request
// ← Valida automaticamente le annotazioni @NotNull, @NotBlank, ecc.
```

### **3. ORM con JPA/Hibernate**
```java
@Entity
@Table(name = "experiences")
public class ExperienceEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    // ← Hibernate gestisce mapping SQL e generazione ID
}
```

### **4. Sicurezza con Spring Security**
```java
@Configuration
public class SecurityConfig {
    // ← Configura:
    // - BCryptPasswordEncoder per hash password
    // - Regole di autorizzazione per rotte
    // - Form di login personalizzato
}
```

---

## 📊 Tabelle del Database

### **admin_users**
```sql
CREATE TABLE admin_users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL  -- BCrypt hash
);
```

### **experiences**
```sql
CREATE TABLE experiences (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    role VARCHAR(255),
    company VARCHAR(255),
    start_date DATE,
    end_date DATE,
    description TEXT
);
```

### **messages**
```sql
CREATE TABLE messages (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(255),
    email VARCHAR(255),
    message TEXT,
    data_invio TIMESTAMP,
    is_read BOOLEAN DEFAULT FALSE
);
```

---

## 🚀 Flusso di Esecuzione dell'Applicazione

```
1. STARTUP
   ├─ PersonalProjectApplication.main()
   │  └─ SpringApplication.run()
   ├─ Spring Boot autoconfigura:
   │  ├─ Tomcat Servlet Container
   │  ├─ DataSource (PostgreSQL/H2)
   │  ├─ JPA/Hibernate
   │  └─ Spring Security
   └─ Applicazione ready su http://localhost:8080

2. RICHIESTA PUBBLICA (GET /)
   ├─ Request arriva al DispatcherServlet
   ├─ SecurityFilterChain controlla:
   │  ├─ CSRF token validato
   │  └─ "/" è in permitAll() → ACCESSO CONCESSO ✅
   ├─ HomeController.home() processato
   ├─ ExperienceService.findAll()
   │  └─ SELECT * FROM experiences
   ├─ Thymeleaf renderizza index.html con le esperienze
   └─ HTTP 200 + HTML response

3. RICHIESTA ADMIN (POST /admin/dashboard)
   ├─ Request arriva al DispatcherServlet
   ├─ SecurityFilterChain controlla:
   │  ├─ "/admin/**" richiede ROLE_ADMIN
   │  ├─ Se no session valida → redirect a /login
   │  └─ Se session OK + ROLE_ADMIN → ACCESSO CONCESSO ✅
   ├─ AdminDashboardController processato
   └─ HTTP 200 + Dashboard HTML

4. LOGIN (POST /login)
   ├─ Username e password inviati
   ├─ AdminUserService.findByUsername() recupera utente da DB
   ├─ BCryptPasswordEncoder.matches() verifica password
   ├─ Se valido:
   │  ├─ Spring Security crea Authentication token
   │  ├─ Sessione marcata come AUTHENTICATED + ROLE_ADMIN
   │  └─ Redirect a /admin/dashboard
   └─ HTTP 302 redirect
```

---

## 💾 DTO (Data Transfer Objects)

### **MessageRequest**
```java
// Rappresenta i dati inviati dal form di contatto
public class MessageRequest {
    private String nome;      // @NotBlank, @NotNull
    private String email;     // @NotBlank, @Email
    private String message;   // @NotBlank
}

// ✅ Serve a:
// - Separare i dati di INPUT dalla struttura persistente (MessageEntity)
// - Validare i dati prima di salvarli
// - Evitare di esporre direttamente l'entità JPA
```

---

## 📝 Convenzioni di Naming

| Tipo | Suffisso | Esempio |
|------|----------|---------|
| Entità JPA | `Entity` | `MessageEntity`, `ExperienceEntity` |
| Controller | (nessuno) | `HomeController`, `AdminDashboardController` |
| Service | `Service` | `MessageService`, `ExperienceService` |
| Repository | `Repository` | `AdminUserRepository`, `ExperienceRepository` |
| DTO | `Request` | `MessageRequest` |
| Template | `.html` | `index.html`, `dashboard.html` |

---

## 🔄 Dipendenze Principali (pom.xml)

```xml
<!-- Spring Boot Web: REST, MVC, Tomcat -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>

<!-- Spring Data JPA: ORM e accesso ai dati -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>

<!-- Spring Security: Autenticazione e autorizzazione -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>

<!-- Thymeleaf: Template engine lato server -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-thymeleaf</artifactId>
</dependency>

<!-- Jakarta Validation: Validazione campi -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-validation</artifactId>
</dependency>

<!-- PostgreSQL Driver -->
<dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>
    <scope>runtime</scope>
</dependency>

<!-- H2 Database (testing) -->
<dependency>
    <groupId>com.h2database</groupId>
    <artifactId>h2</artifactId>
    <scope>runtime</scope>
</dependency>

<!-- Lombok: Annotation processing -->
<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
    <scope>provided</scope>
</dependency>

<!-- Sentry: Error reporting -->
<dependency>
    <groupId>io.sentry</groupId>
    <artifactId>sentry-spring-boot-starter</artifactId>
    <version>6.28.0</version>
</dependency>
```

---

## 🎓 Pattern e Principi SOLID Applicati

| Principio | Implementazione |
|-----------|-----------------|
| **S**ingle Responsibility | Ogni Service ha responsabilità specifica (es. MessageService solo per messaggi) |
| **O**pen/Closed | Facile estendere con nuove entità senza modificare il codice esistente |
| **L**iskov Substitution | Spring Data Repository implementa contratti standard |
| **I**nterface Segregation | DTO specifici per ogni caso d'uso (MessageRequest) |
| **D**ependency Inversion | Dipendenze iniettate via costruttore, non hardcoded |
| **MVC** | Controller → Service → Repository → Model |
| **DAO** | Repository implementa Data Access Object pattern |
| **DTO** | MessageRequest separa dati di input da entità persistente |

---

## ⚙️ Configuration Properties

File: `application.properties`

```properties
# Database Configuration
spring.datasource.url=jdbc:postgresql://localhost:5432/personalproject
spring.datasource.username=postgres
spring.datasource.password=password
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false

# Server
server.port=8080

# Thymeleaf
spring.thymeleaf.cache=false  # Durante lo sviluppo

# Logging
logging.level.root=INFO
logging.level.com.Siw.personalProject=DEBUG

# Sentry
sentry.dsn=${SENTRY_DSN:}  # Opzionale
```

---

## 🧪 Testing

Struttura test:
```
src/test/java/com/Siw/personalProject/
├── PersonalProjectApplicationTests.java  # Test di caricamento context
└── SecurityIntegrationTest.xml           # Test di integrazione sicurezza
```

**Esempi di test:**
```java
@SpringBootTest
public class PersonalProjectApplicationTests {
    @Test
    void contextLoads() {
        // Verifica che lo Spring Context si carichi correttamente
    }
}

// Test della sicurezza
@SpringBootTest
@AutoConfigureMockMvc
public class SecurityIntegrationTest {
    @Test
    void testUnauthorizedAccessToAdmin() {
        // Verifica che /admin/** richieda autenticazione
    }
}
```

---

## 🚨 Considerazioni di Sicurezza

| Aspetto | Status | Note |
|--------|--------|------|
| **Password Hashing** | ✅ BCrypt | Corretto, mai in chiaro |
| **CSRF Protection** | ✅ Disabilitato | Disabilitato per semplicità (considerare riabilitazione) |
| **SQL Injection** | ✅ JPA Prepared Statements | Protetto via Hibernate |
| **Session Hijacking** | ✅ JSESSIONID HttpOnly | Spring Security gestisce |
| **XSS** | ⚠️ Thymeleaf escaping default | Verificare che Thymeleaf non renderizzi HTML non validato |
| **Autenticazione** | ✅ Spring Security | Form-based con sessione |

---

## 📋 Riepilogo dei File Principali

| File | Responsabilità |
|------|-----------------|
| `PersonalProjectApplication.java` | Entry point applicazione |
| `SecurityConfig.java` | Configurazione sicurezza e autorizzazione |
| `HomeController.java` | Gestione homepage e form contatti pubblici |
| `AdminDashboardController.java` | Dashboard amministrativa |
| `AdminExperienceController.java` | Gestione CRUD esperienze |
| `AdminMessageViewController.java` | Visualizzazione messaggi ricevuti |
| `ExperienceService.java` | Logica business esperienze |
| `MessageService.java` | Logica business messaggi |
| `AdminUserService.java` | Logica business utenti admin |
| `AdminUserEntity.java` | Modello utente admin |
| `ExperienceEntity.java` | Modello esperienza lavorativa |
| `MessageEntity.java` | Modello messaggio di contatto |
| `index.html` | Homepage pubblica con portfolio |
| `login.html` | Form di login |
| `dashboard.html` | Dashboard amministrativa |

---

## 🎯 Flusso Tipico di Utilizzo

### Per un **Visitatore**:
```
1. Accede a http://localhost:8080/
2. Vede il portfolio con tutte le esperienze
3. Scrolla fino al form di contatto
4. Compila nome, email, messaggio
5. Clicca "Invia"
6. Vede messaggio di successo
7. Admin riceverà il messaggio
```

### Per l' **Amministratore**:
```
1. Accede a http://localhost:8080/login
2. Inserisce username e password
3. Viene reindirizzato a /admin/dashboard
4. Può:
   - Visualizzare elenco esperienze
   - Aggiungere nuova esperienza
   - Modificare esperienze esistenti
   - Eliminare esperienze
   - Visualizzare messaggi di contatto ricevuti
   - Marcare messaggi come letti
5. Clicca "Logout" per terminare la sessione
```

---

## 🔍 Glossario

| Termine | Significato |
|---------|------------|
| **Entity** | Classe annotata con @Entity, rappresenta una tabella del DB |
| **DTO** | Data Transfer Object, oggetto per trasportare dati tra layer |
| **Service** | Strato di logica di business, contiene regole applicative |
| **Repository** | Interfaccia per accedere ai dati (DAO pattern) |
| **Controller** | Riceve richieste HTTP, delega a Service, ritorna risposta |
| **Template** | File HTML gestito da Thymeleaf lato server |
| **BCrypt** | Algoritmo di hashing per password, computazionalmente sicuro |
| **CSRF** | Cross-Site Request Forgery, attacco di sicurezza (disabilitato qui) |
| **ROLE_ADMIN** | Ruolo di autorizzazione per gli amministratori |
| **Session** | Stato persistente di un utente loggato (JSESSIONID) |
| **JPA** | Jakarta Persistence API, standard ORM Java |
| **Thymeleaf** | Template engine per generare HTML lato server |

---

**Documento Analisi Generato:** 30 Dicembre 2025  
**Java Version:** 21 LTS  
**Spring Boot:** 4.0.1  
**Status:** ✅ Produzione-ready
