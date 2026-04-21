package com.qv.ballet_api.controller;

import com.qv.ballet_api.dto.CanjeRequest;
import com.qv.ballet_api.dto.HistorialResponse;
import com.qv.ballet_api.dto.PagoResponse;
import com.qv.ballet_api.dto.VoucherRequest;
import com.qv.ballet_api.entity.Compra;
import com.qv.ballet_api.entity.Usuario;
import com.qv.ballet_api.entity.Voucher;
import com.qv.ballet_api.repository.CompraRepository;
import com.qv.ballet_api.repository.UsuarioRepository;
import com.qv.ballet_api.service.CanjeService;
import com.qv.ballet_api.service.VoucherService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/taquilla")
@Tag(name = "Canjes", description = "Sistema de canje de códigos y auditoría de historial")
public class PagoController {

    private final CanjeService canjeService;
    private final VoucherService voucherService;
    private final CompraRepository compraRepository;
    private final UsuarioRepository usuarioRepository;


    public PagoController(CanjeService canjeService,
                          VoucherService voucherService,
                          CompraRepository compraRepository,
                          UsuarioRepository usuarioRepository) {
        this.canjeService = canjeService;
        this.voucherService = voucherService;
        this.compraRepository = compraRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @PostMapping("/canjear")
    @Operation(summary = "Canjear código de asientos", description = "Procesa el canje de un voucher por asientos reales")
    public ResponseEntity<PagoResponse> canjearCodigo(@RequestBody CanjeRequest request) {
        PagoResponse resultado = canjeService.procesarCanje(request);
        if ("EXITO".equals(resultado.getEstado())) {
            return ResponseEntity.ok(resultado);
        } else {
            return ResponseEntity.badRequest().body(resultado);
        }
    }

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

    @PostMapping("/admin/generar")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Generar un nuevo Voucher (Admin)")
    public ResponseEntity<Voucher> generarVoucher(@Valid @RequestBody VoucherRequest request) {
        Voucher nuevoVoucher = voucherService.generarVoucher(request.getIdObra(), request.getCantidadAsientos());
        return ResponseEntity.ok(nuevoVoucher);
    }
}