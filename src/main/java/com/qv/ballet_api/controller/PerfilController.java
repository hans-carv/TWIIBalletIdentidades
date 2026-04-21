package com.qv.ballet_api.controller;

import com.qv.ballet_api.dto.HistorialResponse;
import com.qv.ballet_api.entity.Compra;
import com.qv.ballet_api.entity.Usuario;
import com.qv.ballet_api.repository.CompraRepository;
import com.qv.ballet_api.repository.UsuarioRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/perfil")
@Tag(name = "Historial de canjes", description = "Informacion de todos los canjes que hizo el usuario")
public class PerfilController {

    @Autowired private CompraRepository compraRepository;
    @Autowired private UsuarioRepository usuarioRepository;

    @GetMapping("/historial")
    @Operation(summary = "Ver mis compras", description = "Devuelve el historial de tickets del usuario logueado.")
    public ResponseEntity<List<HistorialResponse>> verMiHistorial() {

        String emailUsuario = SecurityContextHolder.getContext().getAuthentication().getName();


        Usuario cliente = usuarioRepository.findByEmail(emailUsuario)
                .orElseThrow(() -> new RuntimeException("Error: Usuario no encontrado en el sistema."));

        List<Compra> comprasDelUsuario = compraRepository.findByUsuario(cliente);
        List<HistorialResponse> historial = new ArrayList<>();

        for (Compra c : comprasDelUsuario) {
            HistorialResponse hr = new HistorialResponse();
            hr.setTicket(c.getCodigoTicket());


            hr.setIdObra(String.valueOf(c.getIdObra()));

            hr.setFechaEvento(c.getFechaEvento());
            hr.setCantidadAsientos(c.getCantidadAsientos());
            hr.setAsientosSeleccionados(c.getAsientosSeleccionados());

            historial.add(hr);
        }

        return ResponseEntity.ok(historial);
    }
}