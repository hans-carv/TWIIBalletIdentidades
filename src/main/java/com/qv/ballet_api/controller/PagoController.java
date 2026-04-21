package com.qv.ballet_api.controller;

import com.qv.ballet_api.dto.CanjeRequest;
import com.qv.ballet_api.dto.PagoResponse;
import com.qv.ballet_api.dto.VoucherRequest;
import com.qv.ballet_api.entity.Voucher;
import com.qv.ballet_api.service.CanjeService;
import com.qv.ballet_api.service.VoucherService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/taquilla")
@Tag(name = "Canjes", description = "Sistema de canje de códigos para entradas")
public class PagoController {

    private final CanjeService canjeService;
    private final VoucherService voucherService;

    public PagoController(CanjeService canjeService, VoucherService voucherService) {
        this.canjeService = canjeService;
        this.voucherService = voucherService;
    }

    @PostMapping("/canjear")
    @Operation(summary = "Canjear código de asientos", description = "Ejemplo de JSON: {\"usuarioId\": 1, \"codigoCanje\": \"VIP-123\", \"asientosIds\": [1, 2]}")
    public ResponseEntity<PagoResponse> canjearCodigo(@RequestBody CanjeRequest request) {

        PagoResponse resultado = canjeService.procesarCanje(request);

        if ("EXITO".equals(resultado.getEstado())) {
            return ResponseEntity.ok(resultado);
        } else {
            return ResponseEntity.badRequest().body(resultado);
        }
    }

    @PostMapping("/admin/generar")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Generar un nuevo Voucher")
    public ResponseEntity<Voucher> generarVoucher(@Valid @RequestBody VoucherRequest request) {
        Voucher nuevoVoucher = voucherService.generarVoucher(request.getIdObra(), request.getCantidadAsientos());
        return ResponseEntity.ok(nuevoVoucher);
    }

}