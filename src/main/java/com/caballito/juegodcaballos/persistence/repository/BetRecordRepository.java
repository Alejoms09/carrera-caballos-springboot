package com.caballito.juegodcaballos.persistence.repository;

import com.caballito.juegodcaballos.persistence.entity.BetRecord;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BetRecordRepository extends JpaRepository<BetRecord, Long> {
    List<BetRecord> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);
}
