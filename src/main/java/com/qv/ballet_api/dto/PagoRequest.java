package com.qv.ballet_api.dto;

import lombok.Data;

@Data
public class PagoRequest {
    private String numeroTarjeta;
    private String NumeroTarjeta;
    private String titular;
    private String mesExpiracion;
    private String anioExpiracion;
    private String cvv;
    private Double montoTotal;
    private Long eventoId;
    private Long usuarioId;
}
