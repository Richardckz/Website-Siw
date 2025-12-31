package com.Siw.personalProject.repository;

import com.Siw.personalProject.model.AdminUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional; // << IMPORT NECESSARIO

@Repository
public interface AdminUserRepository extends JpaRepository<AdminUserEntity, Long> {

    // Controlla se esiste un utente admin con quel username
    boolean existsByUsername(String username);

    // Per login (restituisce Optional per sicurezza)
    Optional<AdminUserEntity> findByUsername(String username);
}
