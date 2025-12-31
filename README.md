# 💼 Personal Portfolio

Un'applicazione **Spring Boot** completa per la gestione di un portfolio personale con area amministrativa protetta.

![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0.1-6DB33F?style=flat-square&logo=springboot)
![Java](https://img.shields.io/badge/Java-21 LTS-ED8B00?style=flat-square&logo=openjdk)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-336791?style=flat-square&logo=postgresql)

---

## 📋 Indice

- [Panoramica](#-panoramica)
- [Caratteristiche](#-caratteristiche)
- [Tecnologie](#-tecnologie)
- [Installazione](#-installazione)
- [Configurazione](#-configurazione)
- [Struttura del Progetto](#-struttura-del-progetto)
- [Usage](#-usage)
- [Sicurezza](#-sicurezza)
- [API e Endpoints](#-api-e-endpoints)
- [Testing](#-testing)
- [Licenza](#-licenza)

---

## 🎯 Panoramica

Questo progetto è un'applicazione web completa che permette di:

- **Visualizzare** un portfolio personale pubblico con esperienze lavorative
- **Gestire** le esperienze professionali tramite CRUD completo
- **Ricevere** messaggi di contatto dai visitatori
- **Amministrare** tutto il contenuto tramite un'area riservata protetta

---

## ✨ Caratteristiche

### 👥 Per i Visitatori
- 🏠 **Homepage** con visualizzazione portfolio
- 📬 **Form di contatto** per inviare messaggi
- 📱 **Design responsive** ottimizzato per tutti i dispositivi

### 🔐 Per gli Amministratori
- 🔑 **Login sicuro** con autenticazione Spring Security
- 📊 **Dashboard** con statistiche riepilogative
- 💼 **Gestione Esperienze** (CRUD completo)
  - Creazione nuove esperienze lavorative
  - Modifica esperienze esistenti
  - Eliminazione esperienze
- 📬 **Gestione Messaggi**
  - Visualizzazione lista messaggi
  - Lettura dettagliata dei messaggi
  - Marcare messaggi come letti

---

## 🛠️ Tecnologie

| Categoria | Tecnologia |
|-----------|------------|
| **Backend** | Spring Boot 4.0.1 |
| **Linguaggio** | Java 21 LTS |
| **Database** | PostgreSQL (produzione), H2 (test) |
| **ORM** | Spring Data JPA / Hibernate |
| **Security** | Spring Security 6.x |
| **Template Engine** | Thymeleaf |
| **Frontend** | Bootstrap 5, jQuery |
| **Build Tool** | Maven |
| **Server** | Apache Tomcat (integrato) |
| **Testing** | JUnit, Spring Test, MockMvc |

---

## 🚀 Installazione

### Prerequisiti

- Java 17 o superiore (raccomandato Java 21)
- Maven 3.6+
- PostgreSQL 14+

### Passaggi

1. **Clona il repository**

```bash
git clone <repository-url>
cd personalProject
```

2. **Configura il database PostgreSQL**

```sql
-- Crea il database
CREATE DATABASE personalproject;

-- (Opzionale) Crea un utente dedicato
CREATE USER your_user WITH PASSWORD 'your_password';
GRANT ALL PRIVILEGES ON DATABASE personalproject TO your_user;
```

3. **Configura le variabili d'ambiente**

```bash
export DB_URL=jdbc:postgresql://localhost:5432/personalproject
export DB_USERNAME=your_username
export DB_PASSWORD=your_password
```

4. **Compila e avvia l'applicazione**

```bash
# Compila il progetto
./mvnw clean package -DskipTests

# Avvia l'applicazione
java -jar target/personalproject-0.0.1-SNAPSHOT.jar
```

5. **Accedi all'applicazione**

L'applicazione sarà disponibile su: **http://localhost:8080**

---

## ⚙️ Configurazione

### File `application.properties`

Il file di configurazione principale si trova in:
```
src/main/resources/application.properties
```

#### Configurazione Database

```properties
# Database PostgreSQL
spring.datasource.url=jdbc:postgresql://localhost:5432/personalproject
spring.datasource.username=postgres
spring.datasource.password=password

# Hibernate / JPA
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
```

#### Configurazione Server

```properties
# Porta del server
server.port=8080

# Thymeleaf (disabilita cache in sviluppo)
spring.thymeleaf.cache=false
```

#### Logging

```properties
# Livelli di logging
logging.level.root=INFO
logging.level.com.Siw.personalProject=DEBUG
```

### Variabili d'Ambiente (Produzione)

Per un deployment in produzione, utilizza variabili d'ambiente:

```bash
export SPRING_DATASOURCE_URL=jdbc:postgresql://your-db-host:5432/personalproject
export SPRING_DATASOURCE_USERNAME=your_db_user
export SPRING_DATASOURCE_PASSWORD=your_db_password
export SENTRY_DSN=your_sentry_dsn  # Opzionale
```

---

## 📁 Struttura del Progetto

```
personalProject/
├── src/
│   ├── main/
│   │   ├── java/com/Siw/personalProject/
│   │   │   ├── PersonalProjectApplication.java    # Entry point
│   │   │   ├── config/
│   │   │   │   ├── SecurityConfig.java            # Configurazione sicurezza
│   │   │   │   ├── GlobalModelAttributes.java     # Attributi globali Thymeleaf
│   │   │   │   └── UserConfig.java                # Configurazione utenti
│   │   │   ├── controller/
│   │   │   │   ├── pub/                           # Controller pubblici
│   │   │   │   │   ├── HomeController.java
│   │   │   │   │   ├── LoginController.java
│   │   │   │   │   ├── PublicContactController.java
│   │   │   │   │   └── PublicExperienceController.java
│   │   │   │   └── admin/                         # Controller admin
│   │   │   │       ├── AdminDashboardController.java
│   │   │   │       ├── AdminExperienceController.java
│   │   │   │       └── AdminMessageViewController.java
│   │   │   ├── model/                             # Entità JPA
│   │   │   │   ├── AdminUserEntity.java
│   │   │   │   ├── ExperienceEntity.java
│   │   │   │   └── MessageEntity.java
│   │   │   ├── repository/                        # Data Access Layer
│   │   │   │   ├── AdminUserRepository.java
│   │   │   │   ├── ExperienceRepository.java
│   │   │   │   └── MessageRepository.java
│   │   │   ├── service/                           # Logica di business
│   │   │   │   ├── AdminUserService.java
│   │   │   │   ├── ExperienceService.java
│   │   │   │   ├── MessageService.java
│   │   │   │   └── ContactService.java
│   │   │   ├── dto/                               # Data Transfer Objects
│   │   │   │   ├── ExperienceDTO.java
│   │   │   │   └── MessageDTO.java
│   │   │   └── request/
│   │   │       └── MessageRequest.java
│   │   └── resources/
│   │       ├── templates/                         # Template Thymeleaf
│   │       │   ├── index.html
│   │       │   ├── contact.html
│   │       │   ├── login.html
│   │       │   └── admin/
│   │       ├── static/                            # File statici
│   │       │   ├── css/style.css
│   │       │   ├── js/index.js
│   │       │   ├── libraries/                     # Bootstrap, jQuery
│   │       │   ├── img/
│   │       │   └── progetti/
│   │       └── application.properties
│   └── test/
│       └── java/com/Siw/personalProject/
│           ├── PersonalProjectApplicationTests.java
│           └── SecurityIntegrationTest.java
├── pom.xml
├── docker-compose.yml
├── Dockerfile
└── README.md
```

---

## 📖 Usage

### Accesso come Amministratore

1. Vai su **http://localhost:8080/login**
2. Inserisci le credenziali di default:
   - **Username:** `*****`
   - **Password:** `*****`
3. Verrai reindirizzato alla dashboard amministrativa

### Gestione Esperienze

Dalla dashboard admin puoi:

- **Visualizzare** tutte le esperienze in una lista
- **Creare** nuove esperienze con:
  - Ruolo/Posizione
  - Nome azienda
  - Data inizio e fine
  - Descrizione dettagliata
- **Modificare** esperienze esistenti
- **Eliminare** esperienze non più necessarie

### Gestione Messaggi

- Visualizza tutti i messaggi ricevuti
- Clicca su un messaggio per leggere il contenuto completo
- I messaggi vengono automaticamente marcati come "letti"

---

## 🔐 Sicurezza

### Autenticazione

- **BCrypt** per l'hashing delle password
- Sessioni gestite da Spring Security
- Protezione CSRF (configurabile)

### Autorizzazione

| Rotta | Permesso |
|-------|----------|
| `/`, `/index`, `/contact` | Pubblico |
| `/css/**`, `/js/**`, `/img/**` | Pubblico |
| `/login` | Pubblico |
| `/admin/**` | Solo ADMIN |
| Altre rotte | Richiedono autenticazione |

### Best Practice Implementate

- ✅ Password hashate con BCrypt
- ✅ Validazione input con Jakarta Validation
- ✅ Separazione tra layer (Controller, Service, Repository)
- ✅ Prepared Statements via JPA/Hibernate

---

## 🌐 API e Endpoints

### Endpoints Pubblici

| Metodo | Rotta | Descrizione |
|--------|-------|-------------|
| GET | `/` | Homepage con portfolio |
| GET | `/index` | Homepage (alternativa) |
| GET | `/contact` | Pagina contatti |
| POST | `/contact` | Invia messaggio di contatto |

### Endpoints Admin

| Metodo | Rotta | Descrizione |
|--------|-------|-------------|
| GET | `/login` | Form di login |
| GET | `/admin/dashboard` | Dashboard amministrativa |
| GET | `/admin/experiences` | Lista esperienze |
| GET | `/admin/experiences/create` | Form creazione esperienza |
| POST | `/admin/experiences` | Salva nuova esperienza |
| GET | `/admin/experiences/edit/{id}` | Form modifica esperienza |
| POST | `/admin/experiences/edit/{id}` | Aggiorna esperienza |
| GET | `/admin/experiences/delete/{id}` | Elimina esperienza |
| GET | `/admin/messages` | Lista messaggi |
| GET | `/admin/messages/view/{id}` | Visualizza messaggio |
| POST | `/admin/messages/view/{id}` | Segna come letto |
| GET | `/logout` | Logout |

---

## 🧪 Testing

### Esecuzione dei Test

```bash
# Esegui tutti i test
./mvnw test

# Esegui test con coverage
./mvnw test jacoco:report
```

### Test Inclusi

- **PersonalProjectApplicationTests**: Verifica il caricamento corretto dello Spring Context
- **SecurityIntegrationTest**: Verifica le regole di sicurezza e autenticazione

### Aggiungere Nuovi Test

```java
@SpringBootTest
@AutoConfigureMockMvc
class YourIntegrationTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @Test
    void testEndpoint() throws Exception {
        mockMvc.perform(get("/endpoint"))
               .andExpect(status().isOk());
    }
}
```

---

## 🐳 Docker

### Build dell'Immagine

```bash
docker build -t personal-portfolio .
```

### Esecuzione con Docker Compose

```bash
docker-compose up -d
```

Questo avvierà:
- L'applicazione Spring Boot sulla porta 8080
- Un database PostgreSQL sulla porta 5432

---

## 📝 Note di Sviluppo

### Creare un Nuovo Amministratore

Puoi creare nuovi amministratori attraverso il database o programmaticamente:

```java
@Autowired
private AdminUserService adminUserService;

public void createAdmin(String username, String password) {
    AdminUserEntity admin = new AdminUserEntity();
    admin.setUsername(username);
    admin.setPassword(passwordEncoder.encode(password));
    adminUserService.save(admin);
}
```

### Aggiungere Nuove Entità

1. Crea l'entità JPA in `model/`
2. Crea il repository in `repository/`
3. Crea il service in `service/`
4. Crea i controller necessari in `controller/`
5. Aggiorna `SecurityConfig.java` se servono nuove autorizzazioni

---

## 📄 Licenza

Questo progetto è distribuito sotto licenza MIT.

---

## 🤝 Contribuire

1. Fai un fork del repository
2. Crea un branch per la tua feature (`git checkout -b feature/AmazingFeature`)
3. Committa le tue modifiche (`git commit -m 'Add some AmazingFeature'`)
4. Pusha il branch (`git push origin feature/AmazingFeature`)
5. Apri una Pull Request

---

## 📧 Contatti

Per domande o supporto, contatta il proprietario del progetto.

---

**Creato con ❤️ utilizzando Spring Boot e Thymeleaf**

