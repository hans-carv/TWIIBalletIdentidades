package com.qv.ballet_api.repository;

import com.qv.ballet_api.entity.Asiento;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AsientoRepository extends JpaRepository<Asiento, Long> {
    List<Asiento> findByObraId(Long obraId);
}