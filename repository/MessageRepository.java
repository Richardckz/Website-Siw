package com.Siw.personalProject.repository;

import com.Siw.personalProject.model.MessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<MessageEntity, Long> {

    // Ordina per data invio (nuovi prima)
    List<MessageEntity> findAllByOrderByDataInvioDesc();

    // Trova tutti i messaggi NON letti
    List<MessageEntity> findByIsReadFalse();
}
