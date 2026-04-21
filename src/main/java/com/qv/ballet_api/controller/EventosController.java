package com.qv.ballet_api.controller;

import com.qv.ballet_api.entity.Obra;
import com.qv.ballet_api.service.EventosService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@Hidden
@RestController
@RequestMapping("/api/eventos")
@Tag(name = "Cartelera", description = "Sincronización de eventos y asientos")
public class EventosController {

    private final EventosService eventosService;

    public EventosController(EventosService eventosService) {
        this.eventosService = eventosService;
    }

    @GetMapping("/sincronizar")
    @Operation(summary = "Sincronizar API Externa", description = "Descarga las obras y genera el mapa de asientos en la BD")
    public ResponseEntity<List<Obra>> sincronizar() {
        return ResponseEntity.ok(eventosService.sincronizarCartelera());
    }
}