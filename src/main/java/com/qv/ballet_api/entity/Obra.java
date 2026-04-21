package com.qv.ballet_api.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Table(name = "obras")
@Data
public class Obra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @NotBlank(message = "Error: El título de la obra no puede estar vacío.")
    private String titulo;

    @NotNull(message = "Error: Debes especificar la fecha del evento.")
    @Future(message = "Error: La fecha debe ser en el futuro.") // <- Opcional, si quieres verte súper pro
    private LocalDate fecha;

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private boolean activo = true;

    @ManyToOne
    @JoinColumn(name = "teatro_id", nullable = false)
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private Teatro teatro;
}