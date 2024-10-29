package com.pprisam.backend.domain.ai.controller;

import com.pprisam.backend.domain.ai.model.ImageAIRequest;
import com.pprisam.backend.domain.ai.service.ImageAIService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/image-ai")
public class ImageAIController {

    private final ImageAIService imageAIService;

    // 이미지 생성
    @PostMapping()
    public String generateImage (@Valid @RequestBody ImageAIRequest imageRequest) {
        return imageAIService.generate(imageRequest.getPrompt());
    }
}
