package com.qv.ballet_api.dto;

import lombok.Data;

@Data
public class HistorialResponse {
    private String ticket;
    private String idObra;
    private String fechaEvento;
    private Integer cantidadAsientos;
    private String asientosSeleccionados;
}