package com.restaurante.arepas21.application.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.restaurante.arepas21.infrastructure.adapter.persistence.entity.TablesEntity;
import com.restaurante.arepas21.infrastructure.adapter.persistence.repository.TableRepositoryImpl;

@Service
public class TablesServiceImpl {

	@Autowired
	TableRepositoryImpl tableRepository;
	
	// Crear mesa
    public String createTable(Integer tableNumber, String status) {
        TablesEntity table = new TablesEntity(null, tableNumber, status, new ArrayList<>());
        tableRepository.save(table);
        return "Mesa creada";
    }

    // Listar todas
    public List<TablesEntity> findAll() {
        return tableRepository.findAll();
    }

    // Buscar por id
    public TablesEntity findById(Long id) {
        return tableRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Mesa no encontrada"));
    }

    // Actualizar estado
    public String updateStatus(Long id, String status) {
        TablesEntity t = findById(id);
        t.setTableStatus(status);
        tableRepository.save(t);
        return "Status actualizado a " + status;
    }

    // NUEVO 👉 obtener sólo el estado de una mesa
    public String getTableStatus(Long id) {
        return findById(id).getTableStatus();
    }
}
