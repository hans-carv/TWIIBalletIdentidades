package com.qv.ballet_api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor

public class PagoResponse {
    private String estado;
    private String mensaje;
}
