package com.restaurante.arepas21.application.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.restaurante.arepas21.infrastructure.adapter.persistence.entity.UserEntity;
import com.restaurante.arepas21.infrastructure.adapter.persistence.repository.UserRepositoryImpl;

@Service
public class AuthServiceImpl {

    @Autowired
    private UserRepositoryImpl userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserEntity register(String username, String password) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new RuntimeException("El usuario ya existe");
        }

        UserEntity user = UserEntity.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .role("ROLE_USER")
                .build();

        return userRepository.save(user);
    }
}