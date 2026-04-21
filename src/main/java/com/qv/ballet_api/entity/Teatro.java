package com.qv.ballet_api.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "teatros")
@Data
public class Teatro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nombre;

    @Schema(description = "Cantidad de filas (Ej: 10 equivale de la A a la J)")
    private int cantidadFilas;

    @Schema(description = "Asientos por cada fila (Ej: 15)")
    private int asientosPorFila;

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private boolean activo = true;
}