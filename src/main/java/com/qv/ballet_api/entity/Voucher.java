package com.qv.ballet_api.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "vouchers_validos")
@Data
public class Voucher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @io.swagger.v3.oas.annotations.media.Schema(accessMode = io.swagger.v3.oas.annotations.media.Schema.AccessMode.READ_ONLY)
    private Long id;

    @Column(nullable = false, unique = true)
    @io.swagger.v3.oas.annotations.media.Schema(accessMode = io.swagger.v3.oas.annotations.media.Schema.AccessMode.READ_ONLY)
    private String codigo;

    @Column(nullable = false)
    private Long idObra;

    @Column(nullable = false)
    private Integer cantidadAsientos;

    @Column(nullable = false)
    @io.swagger.v3.oas.annotations.media.Schema(accessMode = io.swagger.v3.oas.annotations.media.Schema.AccessMode.READ_ONLY)
    private Boolean disponible = true;
}