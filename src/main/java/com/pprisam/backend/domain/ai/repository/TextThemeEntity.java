package com.pprisam.backend.domain.ai.repository;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "text_theme")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TextThemeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String theme;

    @Column(nullable = false)
    private String description;

    public TextThemeEntity(String theme, String description) {
        this.theme = theme;
        this.description = description;
    }
}
