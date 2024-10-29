package com.pprisam.backend.domain.ai.service;

import com.theokanning.openai.image.CreateImageRequest;
import com.theokanning.openai.image.ImageResult;
import com.theokanning.openai.service.OpenAiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.time.Duration;

@Slf4j
@Service
public class ImageAIService {

    private final OpenAiService openAiService;

    public ImageAIService(@Value("${openai.api.key}") String apiKey) {
        this.openAiService = new OpenAiService(apiKey, Duration.ofSeconds(30));
    }

    // 생성된 이미지 URL만 반환
    public String generate(String prompt) {
        log.info("prompt : {}", prompt);

        // 요청 설정
        CreateImageRequest request = CreateImageRequest.builder()
                .prompt(prompt) // 프롬프트 설정
                .model("dall-e-3")  // DALL-E 3 모델 사용
                .n(1) // 생성할 이미지 수
                .build()
                ;

        // 이미지 생성 요청
        ImageResult response = openAiService.createImage(request);

        // 생성된 이미지 URL 반환
        return response.getData().getFirst().getUrl();
    }
}
