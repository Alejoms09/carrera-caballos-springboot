package com.caballito.juegodcaballos.persistence.repository;

import com.caballito.juegodcaballos.persistence.entity.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserAccountRepository extends JpaRepository<UserAccount, Long> {
    Optional<UserAccount> findByUsernameIgnoreCase(String username);

    boolean existsByUsernameIgnoreCase(String username);

    long countByGameGroupId(Long gameGroupId);
}
