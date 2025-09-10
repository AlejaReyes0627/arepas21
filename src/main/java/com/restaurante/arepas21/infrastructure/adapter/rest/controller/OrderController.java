package com.restaurante.arepas21.infrastructure.adapter.rest.controller;

import java.math.BigDecimal;

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

import com.restaurante.arepas21.application.service.OrderServiceImpl;
import com.restaurante.arepas21.infrastructure.adapter.persistence.entity.OrderEntity;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/order")
@Tag(name="Orden", description="Administración de la orden")
@CrossOrigin("*")
@PreAuthorize("hasRole('ADMIN')")
public class OrderController {

	@Autowired
    private OrderServiceImpl orderService;

    @PostMapping("/{tableId}")
    public ResponseEntity<OrderEntity> createOrder(@PathVariable Long tableId) {
        return ResponseEntity.ok(orderService.createOrder(tableId));
    }

    @PostMapping("/{orderId}/items/{productId}")
    public ResponseEntity<OrderEntity> addItem(@PathVariable Long orderId,
                                         @PathVariable Long productId,
                                         @RequestParam int quantity) {
        return ResponseEntity.ok(orderService.addItem(orderId, productId, quantity));
    }

    @PutMapping("/{orderId}/status")
    public ResponseEntity<OrderEntity> updateStatus(@PathVariable Long orderId,
                                              @RequestParam String status) {
        return ResponseEntity.ok(orderService.changeStatus(orderId, status));
    }

    @PostMapping("/{orderId}/pay")
    public ResponseEntity<OrderEntity> registerPayment(@PathVariable Long orderId,
                                                 @RequestParam String method,
                                                 @RequestParam BigDecimal amount) {
        return ResponseEntity.ok(orderService.registerPayment(orderId, method, amount));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderEntity> findById(@PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.findById(orderId));
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<Void> delete(@PathVariable Long orderId) {
        orderService.delete(orderId);
        return ResponseEntity.noContent().build();
    }
    
}
