package com.restaurante.arepas21.infrastructure.adapter.rest.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.restaurante.arepas21.application.service.TablesServiceImpl;
import com.restaurante.arepas21.infrastructure.adapter.persistence.entity.TablesEntity;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/tables")
@Tag(name="Mesas", description="Administración de las mesas")
@CrossOrigin("*")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class TablesController {

	@Autowired
    private TablesServiceImpl tablesService;

    @PostMapping
    public ResponseEntity<String> create(@RequestParam Integer tableNumber,
                                         @RequestParam String status) {
        return ResponseEntity.ok(tablesService.createTable(tableNumber, status));
    }

    @GetMapping
    public ResponseEntity<List<TablesEntity>> getAll() {
        return ResponseEntity.ok(tablesService.findAll());
    }

    @GetMapping("/{tableId}/status")
    public ResponseEntity<String> getStatus(@PathVariable Long tableId) {
        return ResponseEntity.ok(tablesService.getTableStatus(tableId));
    }

    @PutMapping("/{tableId}/status")
    public ResponseEntity<String> updateStatus(@PathVariable Long tableId,
                                               @RequestParam String status) {
        return ResponseEntity.ok(tablesService.updateStatus(tableId, status));
    }

}