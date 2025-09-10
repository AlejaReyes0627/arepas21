package com.restaurante.arepas21.application.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.restaurante.arepas21.infrastructure.adapter.persistence.entity.PaidMethodEntity;
import com.restaurante.arepas21.infrastructure.adapter.persistence.repository.PaidMethodRepositoryImpl;

@Service
public class PaidMethodServiceImpl {

    @Autowired
    private PaidMethodRepositoryImpl repository;

    //Crear nuevo pago
    public PaidMethodEntity create(PaidMethodEntity paidMethod) {
        return repository.save(paidMethod);
    }

    // Eliminar pago por id
    public void delete(Long id) {
        repository.deleteById(id);
    }
    
    // NUEVO -> Buscar método de pago por id
    public List<PaidMethodEntity> findAll() {
        return repository.findAll();
    }
}
