package com.restaurante.arepas21.infrastructure.adapter.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.restaurante.arepas21.infrastructure.adapter.persistence.entity.UserEntity;

import java.util.Optional;

public interface UserRepositoryImpl extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUsername(String username);
}