package com.qv.ballet_api.service;

import com.qv.ballet_api.entity.Voucher;
import com.qv.ballet_api.repository.VoucherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
public class VoucherService {

    @Autowired
    private VoucherRepository voucherRepository;

    private static final String ALFABETO = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private final SecureRandom random = new SecureRandom();

    public Voucher generarVoucher(Long idObra, Integer cantidadAsientos) {


        String codigoGenerado = "VOU-" + generarTexto(6);


        while (voucherRepository.findByCodigo(codigoGenerado).isPresent()) {
            codigoGenerado = "VOU-" + generarTexto(6);
        }


        Voucher nuevoVoucher = new Voucher();
        nuevoVoucher.setCodigo(codigoGenerado);
        nuevoVoucher.setIdObra(idObra);
        nuevoVoucher.setCantidadAsientos(cantidadAsientos);
        nuevoVoucher.setDisponible(true);

        return voucherRepository.save(nuevoVoucher);
    }

    private String generarTexto(int longitud) {
        StringBuilder sb = new StringBuilder(longitud);
        for (int i = 0; i < longitud; i++) {
            sb.append(ALFABETO.charAt(random.nextInt(ALFABETO.length())));
        }
        return sb.toString();
    }
}