package com.qv.ballet_api.controller;

import com.qv.ballet_api.entity.Curso;
import com.qv.ballet_api.entity.Horario;
import com.qv.ballet_api.entity.Novedad;
import com.qv.ballet_api.entity.Obra;
import com.qv.ballet_api.repository.CursoRepository;
import com.qv.ballet_api.repository.NovedadRepository;
import com.qv.ballet_api.repository.ObraRepository;
import com.qv.ballet_api.service.EventosService;
import com.qv.ballet_api.service.NovedadesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/ballet")
@Tag(name = "Novedades y Eventos")
public class BalletController {

    private final CursoRepository cursoRepository;
    private final ObraRepository obraRepository;
    private final NovedadRepository novedadRepository;

    @Autowired
    private EventosService eventosService;

    @Autowired
    private NovedadesService novedadService;

    public BalletController(ObraRepository obraRepository,
                            NovedadRepository novedadRepository,
                            CursoRepository cursoRepository) {
        this.obraRepository = obraRepository;
        this.novedadRepository = novedadRepository;
        this.cursoRepository = cursoRepository;
    }

    @GetMapping("/cartelera")
    @Operation(summary = "Ver cartelera activa (Paginado)")
    public ResponseEntity<Page<Obra>> getCartelera(
            @Parameter(hidden = true)
            @PageableDefault(sort = "id", direction = Sort.Direction.ASC) Pageable pageable,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(obraRepository.findAllByActivoTrue(pageable));
    }

    @PostMapping("/admin/obras")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Crear nueva obra y generar sus asientos")
    public ResponseEntity<Obra> crearObra(
            @Valid @RequestBody Obra obra,
            @RequestParam Long idTeatro) {
        return ResponseEntity.ok(eventosService.crearObraManualmente(obra, idTeatro));
    }

    @PutMapping("/admin/obras/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Editar obra (Actualización parcial)")
    public ResponseEntity<Obra> editarObra(@PathVariable Long id, @RequestBody Obra data) {
        Obra obra = obraRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Obra ID " + id + " no existe"));

        if (data.getTitulo() != null) obra.setTitulo(data.getTitulo());
        if (data.getTeatro() != null) obra.setTeatro(data.getTeatro());
        if (data.getFecha() != null) obra.setFecha(data.getFecha());

        return ResponseEntity.ok(obraRepository.save(obra));
    }



    @DeleteMapping("/admin/obras/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Borrado lógico de obra")
    public ResponseEntity<?> borrarObra(@PathVariable Long id) {
        eventosService.cambiarEstadoObra(id, false);

        Map<String, String> respuesta = new HashMap<>();
        respuesta.put("mensaje", "Obra desactivada correctamente.");
        return ResponseEntity.ok(respuesta);
    }

    @PatchMapping("/admin/obras/{id}/reactivar")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Reactivar obra", description = "Vuelve a poner el estado 'activo' en true.")
    public ResponseEntity<?> reactivarObra(@PathVariable Long id) {
        eventosService.cambiarEstadoObra(id, true);

        Map<String, String> respuesta = new HashMap<>();
        respuesta.put("mensaje", "Obra reactivada con éxito.");
        return ResponseEntity.ok(respuesta);
    }



    @GetMapping("/novedades")
    @Operation(summary = "Ver novedades activas")
    public ResponseEntity<List<Novedad>> getNovedades() {
        return ResponseEntity.ok(novedadRepository.findAllByActivoTrue());
    }

    @PostMapping("/admin/novedades")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Crear nueva novedad")
    public ResponseEntity<Novedad> crearNovedad(@Valid @RequestBody Novedad novedad) {
        novedad.setActivo(true);
        return ResponseEntity.ok(novedadRepository.save(novedad));
    }

    @PutMapping("/admin/novedades/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Editar novedad (Actualización parcial)")
    public ResponseEntity<Novedad> editarNovedad(@PathVariable Long id, @RequestBody Novedad data) {
        Novedad novedadExistente = novedadRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Novedad ID " + id + " no existe."));

        if (data.getTitle() != null) novedadExistente.setTitle(data.getTitle());
        if (data.getBody() != null) novedadExistente.setBody(data.getBody());
        if (data.getImage() != null) novedadExistente.setImage(data.getImage());

        return ResponseEntity.ok(novedadRepository.save(novedadExistente));
    }



    @DeleteMapping("/admin/novedades/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Borrado lógico de novedad")
    public ResponseEntity<?> borrarNovedad(@PathVariable Long id) {
        novedadService.cambiarEstadoNovedad(id, false);

        Map<String, String> respuesta = new HashMap<>();
        respuesta.put("mensaje", "Novedad desactivada correctamente.");
        return ResponseEntity.ok(respuesta);
    }

    @PatchMapping("/admin/novedades/{id}/reactivar")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Reactivar novedad", description = "Vuelve a hacer visible una noticia.")
    public ResponseEntity<?> reactivarNovedad(@PathVariable Long id) {
        novedadService.cambiarEstadoNovedad(id, true);

        Map<String, String> respuesta = new HashMap<>();
        respuesta.put("mensaje", "Novedad reactivada con éxito.");
        return ResponseEntity.ok(respuesta);
    }



    @GetMapping("/cursos")
    @Operation(summary = "Ver cursos y sus horarios", description = "Lista todos los cursos activos con sus respectivos horarios.")
    public ResponseEntity<List<Curso>> getCursos() {
        return ResponseEntity.ok(cursoRepository.findAllByActivoTrue());
    }

    @PostMapping("/admin/cursos")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Crear un curso con múltiples horarios")
    public ResponseEntity<Curso> crearCurso(@Valid @RequestBody Curso curso) {

        if (curso.getHorarios() != null) {
            for (Horario horario : curso.getHorarios()) {
                horario.setCurso(curso);
            }
        }
        curso.setActivo(true);
        return ResponseEntity.ok(cursoRepository.save(curso));
    }
}