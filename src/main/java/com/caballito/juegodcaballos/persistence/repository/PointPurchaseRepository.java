package com.caballito.juegodcaballos.persistence.repository;

import com.caballito.juegodcaballos.persistence.entity.PointPurchase;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PointPurchaseRepository extends JpaRepository<PointPurchase, Long> {
    List<PointPurchase> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);
}
