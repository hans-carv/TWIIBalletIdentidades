package com.qv.ballet_api;

import com.qv.ballet_api.entity.Obra;
import com.qv.ballet_api.entity.Teatro;
import com.qv.ballet_api.repository.AsientoRepository;
import com.qv.ballet_api.repository.ObraRepository;
import com.qv.ballet_api.repository.TeatroRepository;
import com.qv.ballet_api.service.EventosService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventosServiceTest {

	@Mock
	private ObraRepository obraRepository;

	@Mock
	private TeatroRepository teatroRepository;

	@Mock
	private AsientoRepository asientoRepository;

	@InjectMocks
	private EventosService eventosService;

	private Teatro teatroPrueba;
	private Obra obraPrueba;


	@BeforeEach
	void setUp() {
		teatroPrueba = new Teatro();
		teatroPrueba.setId(1L);
		teatroPrueba.setNombre("Teatro de Prueba");
		teatroPrueba.setCantidadFilas(2);
		teatroPrueba.setAsientosPorFila(2);

		obraPrueba = new Obra();
		obraPrueba.setId(1L);
		obraPrueba.setTitulo("El Cascanueces Test");
		obraPrueba.setActivo(true);
	}


	@Test
	void crearObraManualmente_DebeGuardarObraYGenerarAsientos() {

		when(teatroRepository.findById(1L)).thenReturn(Optional.of(teatroPrueba));
		when(obraRepository.save(any(Obra.class))).thenReturn(obraPrueba);

		Obra obraGuardada = eventosService.crearObraManualmente(obraPrueba, 1L);

		assertNotNull(obraGuardada);
		assertEquals("El Cascanueces Test", obraGuardada.getTitulo());


		verify(asientoRepository, times(1)).saveAll(anyList());
	}
	@Test
	void cambiarEstadoObra_DebeDesactivarLaObraExitosamente() {

		when(obraRepository.findById(1L)).thenReturn(Optional.of(obraPrueba));


		eventosService.cambiarEstadoObra(1L, false);


		assertFalse(obraPrueba.isActivo());
		verify(obraRepository, times(1)).save(obraPrueba);
	}


	@Test
	void cambiarEstadoObra_SiYaTieneElMismoEstado_DebeLanzarExcepcion() {

		when(obraRepository.findById(1L)).thenReturn(Optional.of(obraPrueba));


		RuntimeException excepcion = assertThrows(RuntimeException.class, () -> {
			eventosService.cambiarEstadoObra(1L, true);
		});


		assertTrue(excepcion.getMessage().contains("ya se encuentra activada"));


		verify(obraRepository, never()).save(any(Obra.class));
	}
}