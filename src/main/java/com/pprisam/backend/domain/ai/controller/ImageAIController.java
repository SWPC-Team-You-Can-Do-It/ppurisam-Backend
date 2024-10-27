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

    // 생성된 이미지 URL만 반환
    @PostMapping("/url")
    public String generateImage(@Valid @RequestBody ImageAIRequest imageRequest) {
        return imageAIService.generate(imageRequest.getPrompt());
    }

    // 뿌리오 API에 맞게 반환 - JPEG형식을 Base64인코딩 & 생성된 이미지 확인하기 위해 로컬 저장
    @PostMapping("/ppurio")
    public String generateImage2(@Valid @RequestBody ImageAIRequest imageRequest) {
        return imageAIService.generate2(imageRequest.getPrompt());
    }
}
