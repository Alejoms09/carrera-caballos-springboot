package com.caballito.juegodcaballos.service;

import com.caballito.juegodcaballos.persistence.entity.GameGroup;
import com.caballito.juegodcaballos.persistence.entity.UserAccount;
import com.caballito.juegodcaballos.persistence.repository.GameGroupRepository;
import com.caballito.juegodcaballos.persistence.repository.UserAccountRepository;
import com.caballito.juegodcaballos.service.dto.GroupStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserAccountService {

    private final UserAccountRepository userAccountRepository;
    private final GameGroupRepository gameGroupRepository;
    private final PasswordEncoder passwordEncoder;
    private final int maxGroups;
    private final int maxUsersPerGroup;
    private final long initialPoints;

    public UserAccountService(
            UserAccountRepository userAccountRepository,
            GameGroupRepository gameGroupRepository,
            PasswordEncoder passwordEncoder,
            @Value("${app.max-groups:4}") int maxGroups,
            @Value("${app.max-users-per-group:4}") int maxUsersPerGroup,
            @Value("${app.initial-points:1000}") long initialPoints
    ) {
        this.userAccountRepository = userAccountRepository;
        this.gameGroupRepository = gameGroupRepository;
        this.passwordEncoder = passwordEncoder;
        this.maxGroups = maxGroups;
        this.maxUsersPerGroup = maxUsersPerGroup;
        this.initialPoints = initialPoints;
    }

    @Transactional
    public UserAccount register(String username, String rawPassword) {
        String normalizedUsername = normalizeUsername(username);
        validatePassword(rawPassword);

        if (userAccountRepository.existsByUsernameIgnoreCase(normalizedUsername)) {
            throw new BusinessException("El usuario ya existe.");
        }

        long maxUsers = (long) maxGroups * maxUsersPerGroup;
        if (userAccountRepository.count() >= maxUsers) {
            throw new BusinessException("No hay cupos. Limite total: " + maxUsers + " usuarios.");
        }

        GameGroup assignedGroup = findAvailableGroup();
        UserAccount user = new UserAccount();
        user.setUsername(normalizedUsername);
        user.setPasswordHash(passwordEncoder.encode(rawPassword));
        user.setPoints(initialPoints);
        user.setGameGroup(assignedGroup);
        return userAccountRepository.save(user);
    }

    @Transactional(readOnly = true)
    public UserAccount login(String username, String rawPassword) {
        String normalizedUsername = normalizeUsername(username);
        validatePassword(rawPassword);

        UserAccount user = userAccountRepository.findByUsernameIgnoreCase(normalizedUsername)
                .orElseThrow(() -> new BusinessException("Credenciales invalidas."));

        if (!passwordEncoder.matches(rawPassword, user.getPasswordHash())) {
            throw new BusinessException("Credenciales invalidas.");
        }
        return user;
    }

    @Transactional(readOnly = true)
    public Optional<UserAccount> findById(Long userId) {
        return userAccountRepository.findById(userId);
    }

    @Transactional(readOnly = true)
    public List<GroupStatus> getGroupStatuses() {
        List<GameGroup> groups = gameGroupRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
        List<GroupStatus> statuses = new ArrayList<>();
        for (GameGroup group : groups) {
            long usersCount = userAccountRepository.countByGameGroupId(group.getId());
            statuses.add(new GroupStatus(group.getCode(), usersCount, maxUsersPerGroup));
        }
        return statuses;
    }

    private GameGroup findAvailableGroup() {
        List<GameGroup> groups = gameGroupRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
        for (GameGroup group : groups) {
            long usersCount = userAccountRepository.countByGameGroupId(group.getId());
            if (usersCount < maxUsersPerGroup) {
                return group;
            }
        }
        throw new BusinessException("Todos los grupos estan llenos.");
    }

    private String normalizeUsername(String username) {
        if (username == null) {
            throw new BusinessException("El usuario es obligatorio.");
        }
        String value = username.trim();
        if (value.length() < 3 || value.length() > 40) {
            throw new BusinessException("El usuario debe tener entre 3 y 40 caracteres.");
        }
        if (!value.matches("^[a-zA-Z0-9._-]+$")) {
            throw new BusinessException("El usuario solo permite letras, numeros, punto, guion y guion bajo.");
        }
        return value;
    }

    private void validatePassword(String rawPassword) {
        if (rawPassword == null || rawPassword.trim().length() < 6) {
            throw new BusinessException("La contrasena debe tener minimo 6 caracteres.");
        }
    }
}
