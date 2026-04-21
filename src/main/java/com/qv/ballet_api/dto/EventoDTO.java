package com.qv.ballet_api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventoDTO {
    private String titulo;
    private String teatro;
    private String fecha;
}