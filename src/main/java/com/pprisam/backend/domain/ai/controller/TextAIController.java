package com.pprisam.backend.domain.ai.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.pprisam.backend.domain.ai.model.TextAIEditRequest;
import com.pprisam.backend.domain.ai.model.TextAIRequest;
import com.pprisam.backend.domain.ai.model.TextAIResponse;
import com.pprisam.backend.domain.ai.model.ThemeResponse;
import com.pprisam.backend.domain.ai.repository.TextThemeEntity;
import com.pprisam.backend.domain.ai.repository.TextThemeRepository;
import com.pprisam.backend.domain.ai.repository.ThemeEntity;
import com.pprisam.backend.domain.ai.service.TextAIService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/text-ai")
public class TextAIController {

    private final TextAIService textAIService;
    private final TextThemeRepository textThemeRepository;

    @PostMapping("")
    public TextAIResponse generateText(@Valid @RequestBody TextAIRequest request) throws JsonProcessingException {

        return textAIService.generateTextWithTheme(request.getText(), request.getTheme());
    }

    @PostMapping("/edit")
    public TextAIResponse editText(@Valid @RequestBody TextAIEditRequest request) throws JsonProcessingException {

        return textAIService.editText(request.getTextPre(), request.getTextRefactor());
    }

    // 테마 목록 조회
    @GetMapping("/themes")
    public List<ThemeResponse> getThemes() {
        List<TextThemeEntity> themes = textThemeRepository.findAll(); // 모든 테마 데이터를 조회
        return themes.stream()
                .map(theme -> new ThemeResponse(theme.getTheme())) // ThemeResponse로 변환
                .collect(Collectors.toList()); // 리스트 반환
    }
}
