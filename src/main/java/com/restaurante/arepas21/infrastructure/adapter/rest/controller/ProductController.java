package com.restaurante.arepas21.infrastructure.adapter.rest.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.restaurante.arepas21.application.service.ProductServiceImpl;
import com.restaurante.arepas21.infrastructure.adapter.persistence.entity.ProductsEntity;

import org.springframework.web.bind.annotation.RequestBody;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/products")
@Tag(name="Productos", description="Administración de los Productos")
@CrossOrigin("*")
public class ProductController {

	@Autowired
    private ProductServiceImpl productService;

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ProductsEntity> create(@RequestBody ProductsEntity product) {
        return ResponseEntity.ok(productService.create(product));
    }

    @GetMapping
    public ResponseEntity<List<ProductsEntity>> getAll() {
        return ResponseEntity.ok(productService.findAll());
    }

    @PutMapping("/{productId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ProductsEntity> update(@PathVariable Long productId,
                                           @RequestBody ProductsEntity product) {
        return ResponseEntity.ok(productService.update(productId, product));
    }

    @DeleteMapping("/{productId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long productId) {
        productService.delete(productId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<ProductsEntity>> findByName(@RequestParam String name) {
        return ResponseEntity.ok(productService.findByNameContaining(name));
    }
}
