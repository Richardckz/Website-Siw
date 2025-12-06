package com.Siw.personalProject.service;

import com.Siw.personalProject.model.ExperienceEntity;
import com.Siw.personalProject.repository.ExperienceRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ExperienceService {

    private final ExperienceRepository repo;

    public ExperienceService(ExperienceRepository repo) {
        this.repo = repo;
    }

    public List<ExperienceEntity> findAll() {
        return repo.findAll();
    }

    public ExperienceEntity findById(Long id) {
        return repo.findById(id).orElse(null);
    }

    public ExperienceEntity save(ExperienceEntity exp) {
        return repo.save(exp);
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }
}

