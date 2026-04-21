package com.qv.ballet_api.repository;

import com.qv.ballet_api.entity.Obra;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List; // 👈 No olvides importar List

public interface ObraRepository extends JpaRepository<Obra, Long> {

    Page<Obra> findAllByActivoTrue(Pageable pageable);

    List<Obra> findAllByActivoTrue();

    boolean existsByTitulo(String titulo);
}