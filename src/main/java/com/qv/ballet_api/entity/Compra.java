package com.qv.ballet_api.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "compras")
@Data
public class Compra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String codigoTicket;

    @Column(nullable = false)
    private Long idObra;

    private Integer cantidadAsientos;

    private String fechaEvento;

    @Column(unique = true)
    private String codigoCanjeUsado;
    private LocalDateTime fechaCompra = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    @JsonIgnore
    private Usuario usuario;

    private String asientosSeleccionados;
}
