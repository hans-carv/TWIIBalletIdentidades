package com.qv.ballet_api.repository;

import com.qv.ballet_api.entity.Novedad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NovedadRepository extends JpaRepository<Novedad, Long> {

    List<Novedad> findAllByActivoTrue();
}