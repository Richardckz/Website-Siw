package com.Siw.personalProject.service;

import com.Siw.personalProject.model.AdminUserEntity;
import com.Siw.personalProject.repository.AdminUserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AdminUserService {

    private final AdminUserRepository adminUserRepository;

    public AdminUserService(AdminUserRepository adminUserRepository) {
        this.adminUserRepository = adminUserRepository;
    }

    public Optional<AdminUserEntity> findByUsername(String username) {
        return adminUserRepository.findByUsername(username);
    }

    public AdminUserEntity create(AdminUserEntity user) {
        return adminUserRepository.save(user);
    }

    public boolean usernameExists(String username) {
        return adminUserRepository.existsByUsername(username);
    }
}
