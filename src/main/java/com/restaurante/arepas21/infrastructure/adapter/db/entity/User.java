package com.restaurante.arepas21.infrastructure.adapter.db.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users", schema = "arepas21")
@Getter 
@Setter 
@NoArgsConstructor 
@AllArgsConstructor 
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    private String role; // opcional: ROLE_USER, ROLE_ADMIN, etc.
}