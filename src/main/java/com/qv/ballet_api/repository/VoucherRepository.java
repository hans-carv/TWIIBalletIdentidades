package com.qv.ballet_api.repository;
import com.qv.ballet_api.entity.Voucher;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface VoucherRepository extends JpaRepository<Voucher, Long> {
    Optional<Voucher> findByCodigo(String codigo);
}