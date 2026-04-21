package com.qv.ballet_api.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "asientos")
@Data
public class Asiento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "obra_id", nullable = false)
    @JsonIgnore
    private Obra obra;

    private String planta;
    private String fila;
    private Integer numero;

    @Column(nullable = false)
    private Boolean ocupado = false;
}