package com.restaurante.arepas21.infrastructure.adapter.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.restaurante.arepas21.infrastructure.adapter.persistence.entity.OrderEntity;

public interface OrderRepositoryImpl extends JpaRepository<OrderEntity, Long>{

}
