package com.qv.ballet_api.repository;

import com.qv.ballet_api.entity.Curso;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CursoRepository extends JpaRepository<Curso, Long> {
    List<Curso> findAllByActivoTrue();
}