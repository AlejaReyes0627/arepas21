package com.restaurante.arepas21.infrastructure.adapter.rest.controller;

import com.restaurante.arepas21.application.service.AuthServiceImpl;
import com.restaurante.arepas21.application.service.CustomUserDetailsServiceImpl;
import com.restaurante.arepas21.infrastructure.adapter.persistence.entity.UserEntity;
import com.restaurante.arepas21.infrastructure.adapter.rest.dto.UserLogin;
import com.restaurante.arepas21.infrastructure.config.jwtConfig.JwtUtil;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@Tag(name="Autorización de Roles", description="Logueo y registro mediante roles")
@CrossOrigin("*")
public class AuthController {

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private AuthServiceImpl authService;

    @Autowired
    private CustomUserDetailsServiceImpl customUserDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserLogin request, HttpServletResponse response) {
        Authentication authentication = authManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                request.getUsername(), request.getPassword()
            )
        );

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String token = jwtUtil.generateToken(userDetails); // ahora incluye roles

        Cookie cookie = new Cookie("token", token);
        cookie.setHttpOnly(true);
        cookie.setSecure(false); // pon true si usas HTTPS
        cookie.setPath("/");
        cookie.setMaxAge(60 * 60);
        response.addCookie(cookie);

        return ResponseEntity.ok(Map.of("token", token));
    }

    @PostMapping("/register")
    public Map<String, String> register(@RequestBody UserLogin body, HttpServletResponse response) {
        UserEntity newUser = authService.register(body.getUsername(), body.getPassword());
        // ojo: al registrar, normalmente tendrías que asignar un rol al usuario
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(newUser.getUsername());
        String token = jwtUtil.generateToken(userDetails);

        Cookie cookie = new Cookie("token", token);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge(60 * 60);
        response.addCookie(cookie);

        return Map.of("token", token, "message", "Usuario registrado con éxito");
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("token", null);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(HttpServletRequest request) {
        String token = null;

        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("token".equals(cookie.getName())) {
                    token = cookie.getValue();
                    break;
                }
            }
        }

        if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "No hay token"));
        }

        try {
            String username = jwtUtil.extractUsername(token);
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

            if (!jwtUtil.validateToken(token, userDetails)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Token inválido o expirado"));
            }

            return ResponseEntity.ok(Map.of(
                "username", username,
                "roles", jwtUtil.extractAllClaims(token).get("roles")
            ));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Token inválido"));
        }
    }
}
