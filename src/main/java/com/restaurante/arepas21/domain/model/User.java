package com.restaurante.arepas21.domain.model;

import lombok.*;

@Getter 
@Setter 
@NoArgsConstructor 
@AllArgsConstructor 
@Builder
public class User {

    private Long id;

    private String username;

    private String password;

    private String role; 
}