package com.qv.ballet_api.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {

    @NotBlank(message = "Faltan datos: El correo electrónico es obligatorio.")
    private String email;

    @NotBlank(message = "Faltan datos: La contraseña es obligatoria.")
    private String password;
}