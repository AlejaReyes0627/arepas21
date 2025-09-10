package com.restaurante.arepas21.application.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.restaurante.arepas21.infrastructure.adapter.persistence.entity.OrderEntity;
import com.restaurante.arepas21.infrastructure.adapter.persistence.entity.OrderItemEntity;
import com.restaurante.arepas21.infrastructure.adapter.persistence.entity.PaidMethodEntity;
import com.restaurante.arepas21.infrastructure.adapter.persistence.entity.ProductsEntity;
import com.restaurante.arepas21.infrastructure.adapter.persistence.entity.TablesEntity;
import com.restaurante.arepas21.infrastructure.adapter.persistence.repository.OrderRepositoryImpl;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class OrderServiceImpl {

	@Autowired
	private OrderRepositoryImpl orderRepository;
	@Autowired
	private TablesServiceImpl tableService;
	@Autowired
	private ProductServiceImpl productService;
	@Autowired
	private PaidMethodServiceImpl paidMethodService;


	// CREAR ORDEN
    public OrderEntity createOrder(Long tableId) {
        TablesEntity table = tableService.findById(tableId);

        OrderEntity order = new OrderEntity();
        order.setTable(table);
        order.setStatus("PENDIENTE");
        order.setTotalAmount(BigDecimal.ZERO);
        order.setOrderItems(new ArrayList<>());

        tableService.updateStatus(tableId, "OCUPADO");
        return orderRepository.save(order);
    }

    // AGREGAR ITEM
    public OrderEntity addItem(Long orderId, Long productId, int quantity) {
        OrderEntity order = findById(orderId);
        ProductsEntity product = productService.findById(productId);

        OrderItemEntity item = new OrderItemEntity();
        item.setOrder(order);
        item.setProduct(product);
        item.setQuantity(quantity);

        BigDecimal total = BigDecimal.valueOf(product.getPrice())
                .multiply(BigDecimal.valueOf(quantity));
        item.setPrice(total);

        order.getOrderItems().add(item);

        BigDecimal newTotal = order.getOrderItems().stream()
                .map(OrderItemEntity::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        order.setTotalAmount(newTotal);

        return orderRepository.save(order);
    }

    // CAMBIAR ESTADO
    public OrderEntity changeStatus(Long orderId, String status) {
        OrderEntity order = findById(orderId);
        order.setStatus(status);

        if ("PAGADA".equalsIgnoreCase(status)) {
            tableService.updateStatus(order.getTable().getId(), "DISPONIBLE");
        }
        return orderRepository.save(order);
    }

    // REGISTRAR PAGO
    public OrderEntity registerPayment(Long orderId, String paymentMethod, BigDecimal amount) {
        OrderEntity order = findById(orderId);

        PaidMethodEntity paid = PaidMethodEntity.builder()
                .order(order)
                .paymentMethod(paymentMethod)
                .amount(amount.toPlainString())
                .paymentDate(LocalDate.now().toString())
                .build();

        paidMethodService.create(paid);

        order.setStatus("PAGADA");
        tableService.updateStatus(order.getTable().getId(), "DISPONIBLE");
        return orderRepository.save(order);
    }

    // BUSCAR ORDEN
    public OrderEntity findById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Orden no encontrada"));
    }

    // ELIMINAR ORDEN
    public void delete(Long orderId) {
        orderRepository.deleteById(orderId);
    }

}