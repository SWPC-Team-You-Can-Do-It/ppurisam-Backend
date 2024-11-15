package com.pprisam.backend.domain.ai.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ThemeRepository extends JpaRepository<ThemeEntity, Long> {
    Optional<ThemeEntity> findByTheme(String theme);
}
