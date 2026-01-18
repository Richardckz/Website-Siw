# 📊 Domain Model - Website-Siw

## 🎯 Panoramica del Dominio

Questo documento descrive il **Domain Model** dell'applicazione **Website-Siw**, un portfolio personale sviluppato con Spring Boot. Il modello è composto da **3 entità principali** che rappresentano i concetti core del dominio.

---

## 🏗️ Architettura del Modello

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                          WEBSITE-SIW DOMAIN MODEL                           │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                             │
│   ┌──────────────────────┐                                                  │
│   │   AdminUserEntity    │                                                  │
│   │   (Bounded Context:  │                                                  │
│   │    Authentication)   │                                                  │
│   └──────────┬───────────┘                                                  │
│              │                                                              │
│              │ (Nessuna relazione diretta)                                  │
│              │                                                              │
│   ┌──────────┴───────────┐      ┌──────────────────────┐                   │
│   │  ExperienceEntity    │      │    MessageEntity     │                   │
│   │   (Bounded Context:  │      │   (Bounded Context:  │                   │
│   │    Portfolio)        │      │    Communication)    │                   │
│   └──────────────────────┘      └──────────────────────┘                   │
│                                                                             │
└─────────────────────────────────────────────────────────────────────────────┘
```

---

## 📦 Entità del Dominio

### 1️⃣ AdminUserEntity

**Bounded Context:** Authentication  
**Responsabilità:** Gestione delle credenziali di accesso amministrativo

#### Descrizione
Rappresenta un utente amministratore del sistema di portfolio. Questa entità è responsabile dell'autenticazione e dell'autorizzazione degli accessi alla dashboard amministrativa.

#### Attributi

| Attributo | Tipo | Vincoli | Descrizione |
|-----------|------|---------|-------------|
| `id` | Long | @Id, @GeneratedValue | Identificatore univoco (Primary Key) |
| `username` | String | @Column(unique = true, nullable = false) | Username univoco per il login |
| `password` | String | @Column(nullable = false) | Password hashata con BCrypt |

#### Schema Database
```sql
CREATE TABLE admin_users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL
);
```

#### Regole di Business
- La password deve essere **sempre hashata** con BCrypt
- Lo username deve essere **univoco** nel sistema
- Non esistono relazioni con altre entità (entità indipendente)

---

### 2️⃣ ExperienceEntity

**Bounded Context:** Portfolio  
**Responsabilità:** Archiviazione e visualizzazione delle esperienze lavorative

#### Descrizione
Rappresenta un'esperienza lavorativa o professionale da visualizzare nel portfolio pubblico. Ogni esperienza contiene informazioni sul ruolo ricoperto, l'azienda, le date di attività e una descrizione dettagliata.

#### Attributi

| Attributo | Tipo | Vincoli | Descrizione |
|-----------|------|---------|-------------|
| `id` | Long | @Id, @GeneratedValue | Identificatore univoco (Primary Key) |
| `role` | String | - | Posizione/ruolo ricoperto (es. "Senior Developer") |
| `company` | String | - | Nome dell'azienda |
| `startDate` | LocalDate | - | Data di inizio dell'esperienza |
| `endDate` | LocalDate | Nullable | Data di fine (null se ancora in corso) |
| `description` | String | - | Descrizione dettagliata delle attività svolte |

#### Schema Database
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

#### Regole di Business
- `endDate` può essere **null** (l'esperienza è ancora in corso)
- Le esperienze sono ordinate per data (solitamente discendente)
- Non esistono relazioni con altre entità (entità indipendente)

---

### 3️⃣ MessageEntity

**Bounded Context:** Communication  
**Responsabilità:** Gestione dei messaggi di contatto ricevuti dai visitatori

#### Descrizione
Rappresenta un messaggio inviato da un visitatore attraverso il form di contatto. L'entità traccia il mittente, il contenuto, la data di invio e lo stato di lettura.

#### Attributi

| Attributo | Tipo | Vincoli | Descrizione |
|-----------|------|---------|-------------|
| `id` | Long | @Id, @GeneratedValue | Identificatore univoco (Primary Key) |
| `nome` | String | - | Nome del mittente |
| `email` | String | - | Email del mittente |
| `message` | String | @Column(columnDefinition = "TEXT") | Contenuto del messaggio |
| `dataInvio` | LocalDateTime | - | Timestamp di invio del messaggio |
| `isRead` | boolean | Default: false | Flag che indica se il messaggio è stato letto |

#### Schema Database
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

#### Regole di Business
- `isRead` default = **false** (nuovo messaggio non letto)
- `dataInvio` viene impostato automaticamente al momento della creazione
- Non esistono relazioni con altre entità (entità indipendente)

---

## 🔗 Relazioni tra Entità

### Diagramma ER (Entity-Relationship)

```
┌─────────────────────┐
│    admin_users      │
├─────────────────────┤
│ PK: id              │
│ username (UNIQUE)   │
│ password            │
└──────────┬──────────┘
           │
           │ (Nessuna relazione FK)
           │
           ▼
    ┌──────┴──────┐
    │             │
    ▼             ▼
┌────────┐  ┌────────────┐
│experiences│  │  messages  │
├────────┤  ├────────────┤
│ PK: id │  │ PK: id     │
│ role   │  │ nome       │
│ company│  │ email      │
│ ...    │  │ message    │
└────────┘  │ dataInvio  │
            │ isRead     │
            └────────────┘
```

### Note sulle Relazioni
- **Nessuna relazione diretta** tra le entità (no Foreign Keys)
- Ogni entità è **indipendente** e gestisce dati specifici del dominio
- L'assenza di relazioni semplifica la gestione e manutenzione del codice

---

## 🎯 Aggregati

Il dominio è composto da **3 aggregati indipendenti**, ognuno con una radice aggregata propria:

| Aggregato | Root Entity | Entità Contenute |
|-----------|-------------|------------------|
| Authentication | `AdminUserEntity` | Solo la root (nessuna entità figlia) |
| Portfolio | `ExperienceEntity` | Solo la root (nessuna entità figlia) |
| Communication | `MessageEntity` | Solo la root (nessuna entità figlia) |

---

## 📋 Riepilogo Attributi per Entità

### AdminUserEntity
```
+id: Long (PK, Auto-generated)
+username: String (UNIQUE, NOT NULL)
+password: String (NOT NULL, BCrypt hashed)
```

### ExperienceEntity
```
+id: Long (PK, Auto-generated)
+role: String
+company: String
+startDate: LocalDate
+endDate: LocalDate (nullable)
+description: String
```

### MessageEntity
```
+id: Long (PK, Auto-generated)
+nome: String
+email: String
+message: String (TEXT)
+dataInvio: LocalDateTime
+isRead: boolean (default: false)
```

---

## 🔄 Ciclo di Vita delle Entità

### AdminUserEntity
```
Creazione → Autenticazione → (Nessuna modifica frequente)
```

### ExperienceEntity
```
Creazione → Visualizzazione Pubblica → (Opzionale) Modifica/Eliminazione
```

### MessageEntity
```
Invio Form → Salvataggio → Lettura Admin → (Opzionale) Eliminazione
```

---

## 📊 Statistiche del Modello

| Metrica | Valore |
|---------|--------|
| **Numero Entità** | 3 |
| **Numero Aggregati** | 3 |
| **Relazioni tra Entità** | 0 |
| **Entità con Relazioni** | 0 |
| **Bounded Contexts** | 3 |

---

## 🛠️ Stack Tecnologico del Model

| Componente | Tecnologia |
|------------|------------|
| **ORM** | Jakarta Persistence API (JPA) |
| **Implementazione** | Hibernate |
| **Database** | PostgreSQL (produzione), H2 (test) |
| **Lombok** | @Data ( ExperienceEntity) |
| **Validazione** | Jakarta Validation |

---

## 📝 Convenzioni di Naming

| Elemento | Convenzione | Esempio |
|----------|-------------|---------|
| Entità | Suffisso `Entity` | `AdminUserEntity` |
| Tabella DB | Nome al plurale | `admin_users`, `experiences`, `messages` |
| Attributi | camelCase | `startDate`, `dataInvio`, `isRead` |
| Colonne DB | snake_case | `start_date`, `data_invio`, `is_read` |

---

## 📌 Note di Implementazione

### Best Practices Applicate
1. **Separazione delle Responsabilità:** Ogni entità gestisce un solo concetto di dominio
2. **Indipendenza:** Nessuna relazione FK per semplificare la manutenzione
3. **Validazione:** Campi obbligatori marcati con `@Column(nullable = false)`
4. **Tipi Appropriati:** Uso di `LocalDate` per date e `LocalDateTime` per timestamp

### Considerazioni Future
- Possibile aggiunta di relazione tra `MessageEntity` e `AdminUserEntity` per tracciare quale admin ha letto il messaggio
- Possibile aggiunta di tag/categorie per `ExperienceEntity`
- Possibile aggiunta di galleria immagini collegata alle esperienze

---

**Documento Domain Model Generato:** 30 Dicembre 2025  
**Progetto:** Website-Siw  
**Framework:** Spring Boot 4.0.1  
**Versione:** 1.0

