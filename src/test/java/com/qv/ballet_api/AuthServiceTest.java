package com.qv.ballet_api;

import com.qv.ballet_api.entity.Usuario;
import com.qv.ballet_api.repository.UsuarioRepository;
import com.qv.ballet_api.security.JwtUtil;
import com.qv.ballet_api.service.UsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private UsuarioService usuarioService;

    private Usuario usuarioPrueba;

    @BeforeEach
    void setUp() {
        usuarioPrueba = new Usuario();
        usuarioPrueba.setId(1L);
        usuarioPrueba.setEmail("hans@ballet.com");
        usuarioPrueba.setPassword("password123");
        usuarioPrueba.setRol(Usuario.Rol.ROLE_USER);
    }

    @Test
    void registar_DebeEncriptarPasswordYGuardar() {
        when(passwordEncoder.encode("password123")).thenReturn("hash_seguro");
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioPrueba);

        Usuario resultado = usuarioService.registar(usuarioPrueba);

        assertNotNull(resultado);
        assertEquals(Usuario.Rol.ROLE_USER, resultado.getRol());
        assertTrue(resultado.isActivo());
        verify(passwordEncoder, times(1)).encode("password123");
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }

    @Test
    void login_ConCredencialesCorrectas_DebeRetornarToken() {
        when(usuarioRepository.findByEmail("hans@ballet.com")).thenReturn(Optional.of(usuarioPrueba));
        when(passwordEncoder.matches("password123", "password123")).thenReturn(true);
        when(jwtUtil.generarToken("hans@ballet.com", "ROLE_USER")).thenReturn("token_jwt_simulado");

        String token = usuarioService.login("hans@ballet.com", "password123");

        assertEquals("token_jwt_simulado", token);
        verify(jwtUtil, times(1)).generarToken("hans@ballet.com", "ROLE_USER");
    }

    @Test
    void login_ConPasswordIncorrecto_DebeLanzarExcepcion() {
        when(usuarioRepository.findByEmail("hans@ballet.com")).thenReturn(Optional.of(usuarioPrueba));
        when(passwordEncoder.matches("clave_mala", "password123")).thenReturn(false);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            usuarioService.login("hans@ballet.com", "clave_mala");
        });

        assertEquals("Contraseña incorrecta", ex.getMessage());
        verify(jwtUtil, never()).generarToken(anyString(), anyString());
    }

    @Test
    void login_ConUsuarioNoEncontrado_DebeLanzarExcepcion() {
        when(usuarioRepository.findByEmail("desconocido@ballet.com")).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            usuarioService.login("desconocido@ballet.com", "password123");
        });

        assertEquals("Usuario no encontrado", ex.getMessage());
        verify(passwordEncoder, never()).matches(anyString(), anyString());
    }
}