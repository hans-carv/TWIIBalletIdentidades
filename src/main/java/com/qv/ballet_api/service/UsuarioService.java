package com.qv.ballet_api.service;

import com.qv.ballet_api.entity.Usuario;
import com.qv.ballet_api.repository.UsuarioRepository;
import com.qv.ballet_api.security.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public Usuario registar(Usuario usuario) {
        log.info("Registrando nuevo usuario: {}", usuario.getEmail());
        String contraseniaEncriptada = passwordEncoder.encode(usuario.getPassword());
        usuario.setPassword(contraseniaEncriptada);

        usuario.setRol(Usuario.Rol.ROLE_USER);
        usuario.setActivo(true);

        return usuarioRepository.save(usuario);
    }

    public String login(String email, String passwordPlana) {
        log.info("Intento de inicio de sesión para el correo: {}", email);
        Usuario user = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (passwordEncoder.matches(passwordPlana, user.getPassword())) {
            log.info("Login exitoso para: {}", email);
            return jwtUtil.generarToken(email, user.getRol().name());
        } else {
            log.warn("Contraseña incorrecta para el correo: {}", email);
            throw new RuntimeException("Contraseña incorrecta");
        }
    }
}