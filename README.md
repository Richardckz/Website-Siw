# Website-Siw

Portfolio personale sviluppato con Spring Boot.

## Funzionalità

- **Area Pubblica**
  - Visualizzazione esperienze lavorative
  - Form di contatto per inviare messaggi

- **Area Admin (richiede autenticazione)**
  - Dashboard amministrativa
  - Gestione CRUD esperienze (crea, modifica, elimina)
  - Visualizzazione e gestione messaggi ricevuti

## Stack Tecnologico

- Java 21, Spring Boot 4.0.1
- Spring Security, Spring Data JPA
- PostgreSQL / H2
- Thymeleaf, Bootstrap 5, jQuery

## Installazione

```bash
# Build
./mvnw clean package

# Esecuzione
java -jar target/personalProject-0.0.1-SNAPSHOT.jar
```

## Configurazione

Modificare `src/main/resources/application.properties` per il database.

## Credenziali Admin

Creare il primo utente admin nel database con password BCrypt.

