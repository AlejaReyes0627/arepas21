package com.restaurante.arepas21.infrastructure.adapter.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.restaurante.arepas21.application.service.PaidMethodServiceImpl;
import com.restaurante.arepas21.infrastructure.adapter.persistence.entity.PaidMethodEntity;
import org.springframework.web.bind.annotation.RequestBody;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/paidMethod")
@Tag(name="Método de Pago", description="Administración de los métodos de pago")
@CrossOrigin("*")
@PreAuthorize("hasRole('ADMIN')")
public class PaidMethodController {

    @Autowired
    private PaidMethodServiceImpl paidMethodService;

    @PostMapping
    public ResponseEntity<PaidMethodEntity> create(@RequestBody PaidMethodEntity paidMethod) {
        return ResponseEntity.ok(paidMethodService.create(paidMethod));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        paidMethodService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
