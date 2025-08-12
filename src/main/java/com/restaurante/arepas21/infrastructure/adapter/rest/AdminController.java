package com.restaurante.arepas21.infrastructure.adapter.rest;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @GetMapping("/dashboard")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminDashboard() {
        return "Bienvenido al panel de administración. Solo usuarios con ROLE_ADMIN pueden ver esto.";
    }
}
