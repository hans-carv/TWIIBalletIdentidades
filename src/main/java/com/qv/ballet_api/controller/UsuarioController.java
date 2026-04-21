package com.qv.ballet_api.controller;

import com.qv.ballet_api.entity.Usuario;
import com.qv.ballet_api.repository.UsuarioRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequestMapping("/api/admin/usuarios")
@Tag(name = "Gestión de Usuarios", description = "Operaciones administrativas sobre cuentas de usuario")
public class UsuarioController {

    private final UsuarioRepository usuarioRepository;

    public UsuarioController(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @PutMapping("/{id}/ascender")
    @Operation(summary = "Ascender a Administrador", description = "Cambia el rol de un usuario a ROLE_ADMIN (Requiere privilegios de Admin)")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> ascenderAAdmin(@PathVariable Long id) {

        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        usuario.setRol(Usuario.Rol.ROLE_ADMIN);
        usuarioRepository.save(usuario);

        return ResponseEntity.ok("El usuario " + usuario.getNombre() + " ahora es Administrador.");
    }
}