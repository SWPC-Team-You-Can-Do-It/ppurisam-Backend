package com.pprisam.backend.domain.ai.repository;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class ThemeLoader implements CommandLineRunner {
    private final ThemeRepository themeRepository;

    public ThemeLoader(ThemeRepository themeRepository) {
        this.themeRepository = themeRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (themeRepository.count() == 0) {
            themeRepository.save(new ThemeEntity("빈티지/레트로", "Colors and props that contain past vibes (e.g., toned-down colors, film photography vibes)"));
            themeRepository.save(new ThemeEntity("모던/미니멀리즘", "Simple and sophisticated (e.g., modern furniture, white background)"));
            themeRepository.save(new ThemeEntity("판타지/몽환적인", "Surreal and mysterious atmosphere (e.g., magic, starry sky)"));
            themeRepository.save(new ThemeEntity("자연/풍경", "Calmness and wonder given by nature (e.g., mountains, seas, fields)"));
            themeRepository.save(new ThemeEntity("도시/스트리트", "Urban sophistication, street vibrancy (e.g., neon signs, high-rise)"));
            themeRepository.save(new ThemeEntity("고딕/다크", "Dark and mysterious feeling (e.g., antique architecture, dreary atmosphere)"));
            themeRepository.save(new ThemeEntity("로맨틱/사랑스러운", "A lovely, warm atmosphere (e.g., flowers, lovers)"));
            themeRepository.save(new ThemeEntity("아트/추상적", "Feels like colors and textures stand out rather than shapes (e.g., abstract color combinations)"));
        }
    }
}
