package com.restaurante.arepas21.infrastructure.adapter.rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class TablesDto {

    @JsonProperty("numero")
    private Integer tableNumber;
    
    @JsonProperty("estado")
    private String tableStatus;
}
