package com.qv.ballet_api.service;

import com.qv.ballet_api.dto.CanjeRequest;
import com.qv.ballet_api.dto.PagoResponse;
import com.qv.ballet_api.entity.*;
import com.qv.ballet_api.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class CanjeService {

    @Autowired private CompraRepository compraRepository;
    @Autowired private UsuarioRepository usuarioRepository;
    @Autowired private AsientoRepository asientoRepository;
    @Autowired private VoucherRepository voucherRepository;


    @Autowired private ObraRepository obraRepository;

    @Transactional
    public PagoResponse procesarCanje(CanjeRequest request) {

        if (request.getCodigoCanje() == null || request.getAsientosIds() == null || request.getAsientosIds().isEmpty()) {
            return new PagoResponse("ERROR", "Faltan datos o no seleccionaste asientos.");
        }

        String emailUsuario = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario cliente = usuarioRepository.findByEmail(emailUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado en el sistema"));

        String codigoDigitado = request.getCodigoCanje().toUpperCase();

        Voucher voucherValido = voucherRepository.findByCodigo(codigoDigitado).orElse(null);
        if (voucherValido == null) return new PagoResponse("ERROR", "Código inválido.");
        if (!voucherValido.getDisponible()) return new PagoResponse("ERROR", "Este código ya fue utilizado.");

        if (request.getAsientosIds().size() != voucherValido.getCantidadAsientos()) {
            return new PagoResponse("ERROR", "El voucher es para " + voucherValido.getCantidadAsientos() + " asientos.");
        }

        List<Asiento> sillasAComprar = new ArrayList<>();
        StringBuilder textoSillas = new StringBuilder();

        for (Long idAsiento : request.getAsientosIds()) {
            Asiento silla = asientoRepository.findById(idAsiento).orElse(null);
            if (silla == null || silla.getOcupado()) {
                return new PagoResponse("ERROR", "Uno de los asientos no está disponible.");
            }
            silla.setOcupado(true);
            sillasAComprar.add(silla);
            textoSillas.append(silla.getFila()).append(silla.getNumero()).append(" ");
        }

        Obra obraAsociada = obraRepository.findById(voucherValido.getIdObra())
                .orElseThrow(() -> new RuntimeException("Error: La obra asociada a este voucher ya no existe."));

        String ticketFinal = "TICKET-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase();
        Compra nuevaCompra = new Compra();
        nuevaCompra.setCodigoTicket(ticketFinal);
        nuevaCompra.setIdObra(voucherValido.getIdObra());


        nuevaCompra.setFechaEvento(obraAsociada.getFecha().toString());

        nuevaCompra.setCantidadAsientos(voucherValido.getCantidadAsientos());
        nuevaCompra.setAsientosSeleccionados(textoSillas.toString().trim());
        nuevaCompra.setCodigoCanjeUsado(codigoDigitado);
        nuevaCompra.setUsuario(cliente);

        compraRepository.save(nuevaCompra);
        voucherValido.setDisponible(false);
        voucherRepository.save(voucherValido);
        asientoRepository.saveAll(sillasAComprar);

        return new PagoResponse("EXITO", "Compra realizada con éxito. Ticket: " + ticketFinal);
    }
}