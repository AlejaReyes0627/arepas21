package com.restaurante.arepas21.application.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.restaurante.arepas21.infrastructure.adapter.persistence.entity.UserEntity;
import com.restaurante.arepas21.infrastructure.adapter.persistence.repository.UserRepositoryImpl;
import java.util.Collections;

@Service
public class CustomUserDetailsServiceImpl implements UserDetailsService {
    
    private static final Logger logger = LoggerFactory.getLogger(CustomUserDetailsServiceImpl.class);
    
    @Autowired
    private UserRepositoryImpl userRepository;
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.info("🔍 Loading user details for: {}", username);
        
        UserEntity usuario = userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    logger.warn("❌ User not found: {}", username);
                    return new UsernameNotFoundException("Usuario no encontrado: " + username);
                });
        
        logger.info("✅ User found: {}", usuario.getUsername());
        logger.info("  Role from DB: '{}'", usuario.getRole());
        
        UserDetails userDetails = org.springframework.security.core.userdetails.User
                .withUsername(usuario.getUsername())
                .password(usuario.getPassword())
                .authorities(Collections.singletonList(new SimpleGrantedAuthority(usuario.getRole())))
                .build();
        
        logger.info("✅ UserDetails created successfully");
        logger.info("  Username: {}", userDetails.getUsername());
        logger.info("  Authorities: {}", userDetails.getAuthorities());
        
        return userDetails;
    }
}