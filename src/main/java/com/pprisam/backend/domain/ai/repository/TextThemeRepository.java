package com.pprisam.backend.domain.ai.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TextThemeRepository extends JpaRepository<TextThemeEntity, Long> {
    Optional<TextThemeEntity> findByTheme(String theme);
}
