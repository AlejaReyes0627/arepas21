package com.restaurante.arepas21.application.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.restaurante.arepas21.infrastructure.adapter.persistence.entity.ProductsEntity;
import com.restaurante.arepas21.infrastructure.adapter.persistence.repository.ProductRepositoryImpl;

@Service
public class ProductServiceImpl {
	
	@Autowired
	private ProductRepositoryImpl productRepository;

	// Buscar por id
    public ProductsEntity findById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
    }

    // Listar todos
    public List<ProductsEntity> findAll() {
        return productRepository.findAll();
    }

    // AGREGAR
    public ProductsEntity create(ProductsEntity product) {
        return productRepository.save(product);
    }

    // ACTUALIZAR
    public ProductsEntity update(Long id, ProductsEntity update) {
        ProductsEntity p = findById(id);
        p.setName(update.getName());
        p.setPrice(update.getPrice());
        p.setDescription(update.getDescription());
        p.setAvailable(update.getAvailable());
        return productRepository.save(p);
    }

    // ELIMINAR
    public void delete(Long id) {
        productRepository.deleteById(id);
    }

	public List<ProductsEntity> findByNameContaining(String name) {
		// TODO Auto-generated method stub
		return productRepository.findByNameContainingIgnoreCase(name);
	}

}
