package com.restaurante.arepas21.infrastructure.adapter.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.restaurante.arepas21.infrastructure.adapter.persistence.entity.ProductsEntity;

public interface ProductRepositoryImpl extends JpaRepository<ProductsEntity, Long>{

	List<ProductsEntity> findByNameContainingIgnoreCase(String name);

}
