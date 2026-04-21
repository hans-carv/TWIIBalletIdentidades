package com.qv.ballet_api.controller;

import com.qv.ballet_api.entity.Compra;
import com.qv.ballet_api.entity.Usuario;
import com.qv.ballet_api.repository.CompraRepository;
import com.qv.ballet_api.repository.UsuarioRepository;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

@RestController
@RequestMapping("/api/admin/usuarios")
@Tag(name = "Historial de canjes", description = "Información de todos los canjes que hizo el usuario")
public class UsuarioController {

    private final CompraRepository compraRepository;
    private final UsuarioRepository usuarioRepository;

    public UsuarioController(CompraRepository compraRepository, UsuarioRepository usuarioRepository) {
        this.compraRepository = compraRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Hidden
    @GetMapping("/{usuarioID}/historial")
    @Operation(summary = "Auditar compras de un cliente", description = "Devuelve todos los tickets de un usuario (Requiere Admin)")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getHistorial(@PathVariable Long usuarioID) {

        Usuario cliente = usuarioRepository.findById(usuarioID).orElse(null);

        if (cliente == null) {
            return ResponseEntity.badRequest().body("Error: El usuario con ID " + usuarioID + " no existe.");
        }

        List<Compra> historial = compraRepository.findByUsuario(cliente);
        return ResponseEntity.ok(historial);
    }

    @PutMapping("/{id}/ascender")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> ascenderAAdmin(@PathVariable Long id) {

        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        usuario.setRol(Usuario.Rol.ROLE_ADMIN);
        usuarioRepository.save(usuario);

        return ResponseEntity.ok("El usuario " + usuario.getNombre() + " ahora es Administrador.");
    }
}