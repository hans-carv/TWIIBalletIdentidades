package com.qv.ballet_api.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cursos")
@Data
public class Curso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @NotBlank(message = "Error: El nivel del curso es obligatorio (Ej. Principiantes, Avanzados).")
    @Schema(example = "Principiantes")
    private String nivel;

    @NotBlank(message = "Error: Debes asignar un profesor al curso.")
    @Schema(example = "Prof. Vargas")
    private String profesor;

    @NotEmpty(message = "Error: El curso debe tener al menos un horario asignado.")
    @Valid
    @OneToMany(mappedBy = "curso", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Horario> horarios = new ArrayList<>();

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private boolean activo = true;
}