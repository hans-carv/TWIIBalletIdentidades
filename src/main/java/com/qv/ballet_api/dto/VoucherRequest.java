package com.qv.ballet_api.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class VoucherRequest {

    @NotNull(message = "Error: El ID de la obra es obligatorio para generar el voucher.")
    private Long idObra;

    @NotNull(message = "Error: Debes especificar la cantidad de asientos que cubre este voucher.")
    @Min(value = 1, message = "Error: El voucher debe ser válido para al menos 1 asiento.")
    private Integer cantidadAsientos;
}