package com.qv.ballet_api.service;

import com.qv.ballet_api.dto.EventoDTO;
import com.qv.ballet_api.entity.Asiento;
import com.qv.ballet_api.entity.Obra;
import com.qv.ballet_api.entity.Teatro;
import com.qv.ballet_api.repository.AsientoRepository;
import com.qv.ballet_api.repository.ObraRepository;
import com.qv.ballet_api.repository.TeatroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class EventosService {

    @Autowired
    private ObraRepository obraRepository;

    @Autowired
    private AsientoRepository asientoRepository;

    @Autowired
    private TeatroRepository teatroRepository;

    @Transactional
    public List<Obra> sincronizarCartelera() {

        List<EventoDTO> apiExterna = Arrays.asList(
                new EventoDTO("El Lago de los Cisnes", "Teatro Principal", "2026-05-15" ),
                new EventoDTO("El Cascanueces", "Auditorio Nacional", "2026-12-24")
        );

        List<Obra> obrasActualizadas = new ArrayList<>();

        for (EventoDTO evento : apiExterna) {

            if (!obraRepository.existsByTitulo(evento.getTitulo())) {


                Teatro teatroEncontrado = teatroRepository.findByNombre(evento.getTeatro())
                        .orElseGet(() -> {
                            Teatro nuevoTeatro = new Teatro();
                            nuevoTeatro.setNombre(evento.getTeatro());
                            nuevoTeatro.setCantidadFilas(8);
                            nuevoTeatro.setAsientosPorFila(10);
                            nuevoTeatro.setActivo(true);
                            return teatroRepository.save(nuevoTeatro);
                        });

                Obra nuevaObra = new Obra();
                nuevaObra.setTitulo(evento.getTitulo());
                nuevaObra.setTeatro(teatroEncontrado);
                nuevaObra.setFecha(java.time.LocalDate.parse(evento.getFecha()));
                nuevaObra.setActivo(true);

                Obra obraGuardada = obraRepository.save(nuevaObra);


                List<Asiento> nuevosAsientos = new ArrayList<>();
                String alfabeto = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

                for (int fila = 0; fila < teatroEncontrado.getCantidadFilas(); fila++) {
                    String letraFila = String.valueOf(alfabeto.charAt(fila));

                    for (int num = 1; num <= teatroEncontrado.getAsientosPorFila(); num++) {
                        Asiento silla = new Asiento();
                        silla.setObra(obraGuardada);
                        silla.setFila(letraFila);
                        silla.setNumero(num);
                        silla.setOcupado(false);


                        if (fila < (teatroEncontrado.getCantidadFilas() / 2)) {
                            silla.setPlanta("Planta Baja");
                        } else {
                            silla.setPlanta("Planta Alta");
                        }

                        nuevosAsientos.add(silla);
                    }
                }

                asientoRepository.saveAll(nuevosAsientos);
                obrasActualizadas.add(obraGuardada);
            }
        }

        return obraRepository.findAllByActivoTrue();
    }

    public List<Obra> obtenerCartelera() {
        return obraRepository.findAllByActivoTrue();
    }


    @Transactional
    public Obra crearObraManualmente(Obra nuevaObra, Long idTeatro) {


        Teatro teatro = teatroRepository.findById(idTeatro)
                .orElseThrow(() -> new RuntimeException("El Teatro con ID " + idTeatro + " no existe."));


        nuevaObra.setTeatro(teatro);
        nuevaObra.setActivo(true);
        Obra obraGuardada = obraRepository.save(nuevaObra);


        List<Asiento> nuevosAsientos = new ArrayList<>();
        String alfabeto = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

        for (int fila = 0; fila < teatro.getCantidadFilas(); fila++) {
            String letraFila = String.valueOf(alfabeto.charAt(fila));

            for (int num = 1; num <= teatro.getAsientosPorFila(); num++) {
                Asiento silla = new Asiento();
                silla.setObra(obraGuardada);
                silla.setFila(letraFila);
                silla.setNumero(num);
                silla.setOcupado(false);

                if (fila < (teatro.getCantidadFilas() / 2)) {
                    silla.setPlanta("Planta Baja");
                } else {
                    silla.setPlanta("Planta Alta");
                }
                nuevosAsientos.add(silla);
            }
        }
        asientoRepository.saveAll(nuevosAsientos);

        return obraGuardada;
    }

    public void cambiarEstadoObra(Long id, boolean estado) {
        Obra obra = obraRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Error: La obra con ID " + id + " no existe en el sistema."));
        if (obra.isActivo() == estado) {
            String estadoTexto = estado ? "activada" : "desactivada";
            throw new RuntimeException("Error de Operación: La obra ya se encuentra " + estadoTexto + " actualmente.");
        }
        obra.setActivo(estado);
        obraRepository.save(obra);
    }
}