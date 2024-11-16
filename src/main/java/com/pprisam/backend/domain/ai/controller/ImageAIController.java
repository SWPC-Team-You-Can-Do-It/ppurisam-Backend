package com.pprisam.backend.domain.ai.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.pprisam.backend.domain.ai.model.ImageAIRequest;
import com.pprisam.backend.domain.ai.model.ThemeResponse;
import com.pprisam.backend.domain.ai.repository.ThemeEntity;
import com.pprisam.backend.domain.ai.repository.ThemeRepository;
import com.pprisam.backend.domain.ai.service.ImageAIService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/image-ai")
public class ImageAIController {

    private final ImageAIService imageAIService;
    private final ThemeRepository themeRepository;

    // 이미지 생성
    @PostMapping()
    public String generateImage (@Valid @RequestBody ImageAIRequest imageRequest) throws JsonProcessingException {
        return imageAIService.generate(imageRequest.getPrompt(), imageRequest.getTheme());
    }

    // 테마 목록 조회
    @GetMapping("/themes")
    public List<ThemeResponse> getThemes() {
        List<ThemeEntity> themes = themeRepository.findAll(); // 모든 테마 데이터를 조회
        return themes.stream()
                .map(theme -> new ThemeResponse(theme.getTheme())) // ThemeResponse로 변환
                .collect(Collectors.toList()); // 리스트 반환
    }
}
