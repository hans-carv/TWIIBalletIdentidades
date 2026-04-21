package com.qv.ballet_api.controller;

import com.qv.ballet_api.dto.LoginRequest;
import com.qv.ballet_api.dto.RegistroRequest;
import com.qv.ballet_api.entity.Usuario;
import com.qv.ballet_api.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Autenticación", description = "Registro e inicio de sesión de usuarios con JWT")
public class AuthController {

    private final UsuarioService usuarioService;

    public AuthController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping("/registro")
    public ResponseEntity<Usuario> registrarUsuario(@Valid @RequestBody RegistroRequest request) {

        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setNombre(request.getNombre());
        nuevoUsuario.setEmail(request.getEmail());
        nuevoUsuario.setPassword(request.getPassword());

        Usuario usuarioGuardado = usuarioService.registar(nuevoUsuario);

        return ResponseEntity.ok(usuarioGuardado);
    }

    @PostMapping("/inicioSesion")
    @Operation(summary = "Inicio de sesión", description = "Devuelve el Token JWT si las credenciales son correctas")
    public ResponseEntity<?> iniciarSesion(@Valid @RequestBody LoginRequest loginRequest) {
        try {

            String token = usuarioService.login(loginRequest.getEmail(), loginRequest.getPassword());

            Map<String, String> response = new HashMap<>();
            response.put("token", token);

            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }

    }
}