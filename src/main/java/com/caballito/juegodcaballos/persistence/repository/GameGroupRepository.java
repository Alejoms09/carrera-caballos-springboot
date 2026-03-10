package com.caballito.juegodcaballos.persistence.repository;

import com.caballito.juegodcaballos.persistence.entity.GameGroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GameGroupRepository extends JpaRepository<GameGroup, Long> {
    Optional<GameGroup> findByCode(String code);
}
