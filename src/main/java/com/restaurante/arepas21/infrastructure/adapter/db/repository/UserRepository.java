package com.restaurante.arepas21.infrastructure.adapter.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.restaurante.arepas21.infrastructure.adapter.db.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}