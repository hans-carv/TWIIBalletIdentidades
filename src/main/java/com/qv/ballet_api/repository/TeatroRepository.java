package com.qv.ballet_api.repository;

import com.qv.ballet_api.entity.Teatro;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface TeatroRepository extends JpaRepository<Teatro, Long> {
    Optional<Teatro> findByNombre(String nombre);
}