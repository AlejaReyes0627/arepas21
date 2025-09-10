package com.restaurante.arepas21.infrastructure.adapter.persistence.entity;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "tables", schema = "arepas21")
@Getter 
@Setter 
@NoArgsConstructor 
@AllArgsConstructor 
@Builder
@ToString(exclude = "orders") // Evita referencias circulares
@EqualsAndHashCode(exclude = "orders") // Evita referencias circulares
public class TablesEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="table_number")
    private Integer tableNumber;
    
    @Column(name="table_status")
    private String tableStatus;
    
    @OneToMany(mappedBy = "table", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<OrderEntity> orders;
}