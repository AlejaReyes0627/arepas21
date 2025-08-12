package com.restaurante.arepas21.infrastructure.adapter.rest;

import com.restaurante.arepas21.application.service.AuthService;
import com.restaurante.arepas21.infrastructure.adapter.db.entity.User;
import com.restaurante.arepas21.infrastructure.config.security.JwtUtil;

import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.*;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@Tag(name="Autorización de Roles", description="logueo y registro mediante roles")
public class AuthController {

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private AuthService authService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String password = body.get("password");

        authManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        String token = jwtUtil.generateToken(username);

        return Map.of("token", token);
    }

    @PostMapping("/register")
    public Map<String, String> register(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String password = body.get("password");

        User newUser = authService.register(username, password);
        String token = jwtUtil.generateToken(newUser.getUsername());

        return Map.of("token", token, "message", "Usuario registrado con éxito");
    }
}