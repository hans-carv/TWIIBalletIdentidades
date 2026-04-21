package com.qv.ballet_api.repository;

import com.qv.ballet_api.entity.Compra;
import com.qv.ballet_api.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CompraRepository extends JpaRepository<Compra, Long> {
    List<Compra> findByUsuario(Usuario usuario);
    boolean existsByCodigoCanjeUsado(String codigoCanjeUsado);
}
