package com.qv.ballet_api.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Entity
@Table(name = "novedades")
@Data
public class Novedad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @NotBlank(message = "Error: La noticia debe tener un título.")
    private String title;

    @NotBlank(message = "Error: El cuerpo de la noticia es obligatorio.")
    private String body;

    private String image;

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private boolean activo = true;
}