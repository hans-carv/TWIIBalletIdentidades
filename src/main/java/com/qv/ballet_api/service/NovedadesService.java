package com.qv.ballet_api.service;

import com.qv.ballet_api.entity.Novedad;
import com.qv.ballet_api.repository.NovedadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NovedadesService {

    @Autowired
    private NovedadRepository novedadRepository;

    public void cambiarEstadoNovedad(Long id, boolean estado) {
        Novedad novedad = novedadRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Error: La novedad con ID " + id + " no existe."));

        if (novedad.isActivo() == estado) {
            String estadoTexto = estado ? "activada" : "desactivada";
            throw new RuntimeException("Error de Operación: La novedad ya se encuentra " + estadoTexto + " actualmente.");
        }

        novedad.setActivo(estado);
        novedadRepository.save(novedad);
    }
}