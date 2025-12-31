package com.Siw.personalProject.service;

import com.Siw.personalProject.model.ExperienceEntity;
import com.Siw.personalProject.repository.ExperienceRepository;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * 📦 SERVICE LAYER: ExperienceService
 * ═════════════════════════════════════════════════════════════════════════════════
 * 
 * RESPONSABILITÀ: Implementa la logica di business per le esperienze lavorative.
 * Questo service funge da intermediario tra i Controller e il Repository.
 * 
 * 🏗️ PATTERN APPLICATO:
 *    - Service Layer: Contiene tutta la logica di business
 *    - Dependency Injection: Repository iniettato via costruttore
 *    - Single Responsibility: Solo operazioni su ExperienceEntity
 * 
 * 📊 DOMAIN MODEL:
 *    Gestisce il BOUNDED CONTEXT "PORTFOLIO":
 *    - Entità: ExperienceEntity
 *    - Operazioni: CRUD (Create, Read, Update, Delete)
 *    - Validazioni: Possono essere estese qui (date coerenti, etc.)
 * 
 * 🎬 CASI D'USO CORRELATI:
 *    - UC1: Visualizzazione Portfolio (findAll)
 *    - UC5a: Creazione Esperienza (save)
 *    - UC5b: Visualizzazione Lista (findAll)
 *    - UC5c: Modifica Esperienza (save)
 *    - UC5d: Eliminazione Esperienza (delete)
 * 
 * 💡 FLUSSO TIPICO:
 *    Controller riceve richiesta HTTP
 *      ↓
 *    Controller chiama ExperienceService.save(entity)
 *      ↓
 *    ExperienceService applica logica di business
 *      ↓
 *    ExperienceService delega a ExperienceRepository.save()
 *      ↓
 *    Repository esegue query SQL via JPA/Hibernate
 *      ↓
 *    Database risponde
 *      ↓
 *    Controller ritorna risposta HTTP
 */
@Service
public class ExperienceService {

    /**
     * Repository per accedere ai dati delle esperienze dal database.
     * Iniettato automaticamente da Spring via Dependency Injection.
     */
    private final ExperienceRepository repo;

    /**
     * Costruttore con Dependency Injection.
     * Spring automaticamente fornisce un'istanza di ExperienceRepository.
     * 
     * ✅ VANTAGGI:
     *    - Testabile: posso mockare il repository nei test
     *    - Disaccoppiato: il service non conosce come ottenere il repository
     *    - Configurabile: Spring gestisce l'istanza
     */
    public ExperienceService(ExperienceRepository repo) {
        this.repo = repo;
    }

    /**
     * 🔵 Recupera TUTTE le esperienze dal database.
     * 
     * 📝 USATO IN:
     *    - HomeController.home() - Per mostrare il portfolio pubblico
     *    - AdminExperienceController - Per mostrare la lista admin
     * 
     * 💾 QUERY DATABASE:
     *    SELECT * FROM experiences
     * 
     * @return List di tutte le ExperienceEntity presenti nel DB
     *         (lista vuota se nessuna esperienza)
     */
    public List<ExperienceEntity> findAll() {
        return repo.findAll();
    }

    /**
     * 🔵 Recupera una SINGOLA esperienza per ID.
     * 
     * 📝 USATO IN:
     *    - AdminExperienceController - Per mostrare il form di modifica
     *    - AdminExperienceController - Per validare che esista prima di modificare
     * 
     * 💾 QUERY DATABASE:
     *    SELECT * FROM experiences WHERE id = ?
     * 
     * @param id - L'ID della esperienza da recuperare
     * @return L'ExperienceEntity trovata, o NULL se non esiste
     * 
     * 💡 NOTA: Ritorna null anziché lanciare eccezione.
     *    Utile per check di esistenza.
     */
    public ExperienceEntity findById(Long id) {
        return repo.findById(id).orElse(null);
    }

    /**
     * 🔵 Salva (crea o aggiorna) un'esperienza.
     * 
     * 📝 USATO IN:
     *    - AdminExperienceController (POST /admin/experiences) - Creazione nuova
     *    - AdminExperienceController (PUT /admin/experiences/{id}) - Aggiornamento
     * 
     * 💾 QUERY DATABASE:
     *    INSERT INTO experiences (...) VALUES (...)
     *    O
     *    UPDATE experiences SET ... WHERE id = ?
     * 
     * 💡 LOGICA HIBERNATE:
     *    - Se exp.id è null → INSERT (creazione)
     *    - Se exp.id ha valore → UPDATE (aggiornamento)
     * 
     * @param exp - L'ExperienceEntity da salvare
     * @return L'entity salvata con ID generato (in caso di INSERT)
     * 
     * 🔧 ESTENSIONI FUTURE:
     *    Qui potremmo aggiungere validazioni:
     *    - Controllare che endDate >= startDate
     *    - Controllare che role non sia vuoto
     *    - Registrare un audit log
     */
    public ExperienceEntity save(ExperienceEntity exp) {
        return repo.save(exp);
    }

    /**
     * 🔵 Elimina un'esperienza per ID.
     * 
     * 📝 USATO IN:
     *    - AdminExperienceController (DELETE /admin/experiences/{id})
     * 
     * 💾 QUERY DATABASE:
     *    DELETE FROM experiences WHERE id = ?
     * 
     * @param id - L'ID dell'esperienza da eliminare
     * 
     * ⚠️ ATTENZIONE:
     *    - Non controlla se l'ID esiste prima di eliminare
     *    - Se ID non esiste, nessun errore (comportamento JPA standard)
     *    - Considerate di aggiungere una validazione
     * 
     * 🔧 MIGLIORAMENTO SUGGERITO:
     *    public void delete(Long id) {
     *        if (!repo.existsById(id)) {
     *            throw new ResourceNotFoundException("Esperienza non trovata");
     *        }
     *        repo.deleteById(id);
     *    }
     */
    public void delete(Long id) {
        repo.deleteById(id);
    }
}

