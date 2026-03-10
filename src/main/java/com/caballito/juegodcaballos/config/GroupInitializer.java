package com.caballito.juegodcaballos.config;

import com.caballito.juegodcaballos.persistence.entity.GameGroup;
import com.caballito.juegodcaballos.persistence.repository.GameGroupRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Configuration
public class GroupInitializer {

    @Bean
    @Transactional
    public ApplicationRunner initializeGroups(
            GameGroupRepository groupRepository,
            @Value("${app.max-groups:4}") int maxGroups
    ) {
        return args -> {
            Set<String> existingCodes = new HashSet<>();
            groupRepository.findAll().forEach(group -> existingCodes.add(group.getCode()));

            for (int i = 1; i <= maxGroups; i++) {
                String code = "GRUPO-" + i;
                if (existingCodes.contains(code)) {
                    continue;
                }

                GameGroup group = new GameGroup();
                group.setCode(code);
                groupRepository.save(group);
            }
        };
    }
}
