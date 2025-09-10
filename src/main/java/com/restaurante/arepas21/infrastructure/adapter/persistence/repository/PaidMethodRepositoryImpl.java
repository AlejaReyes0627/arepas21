package com.restaurante.arepas21.infrastructure.adapter.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.restaurante.arepas21.infrastructure.adapter.persistence.entity.PaidMethodEntity;

public interface PaidMethodRepositoryImpl extends JpaRepository<PaidMethodEntity, Long>{

}
