package com.qv.ballet_api.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.ToString;

@Entity
@Table(name = "horarios")
@Data
public class Horario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Column(nullable = false)
    @Schema(example = "Lunes")
    @NotBlank(message = "Error: Debes especificar el día (Ej. Lunes).")
    private String dias;

    @Column(nullable = false)
    @Schema(example = "16:00")
    @NotBlank(message = "Error: Debes especificar la hora de inicio.")
    private String horaInicio;

    @Column(nullable = false)
    @Schema(example = "17:30")
    @NotBlank(message = "Error: Debes especificar la hora de fin.")
    private String horaFin;

    @ManyToOne
    @JoinColumn(name = "curso_id", nullable = false)
    @JsonIgnore
    @ToString.Exclude
    private Curso curso;
}