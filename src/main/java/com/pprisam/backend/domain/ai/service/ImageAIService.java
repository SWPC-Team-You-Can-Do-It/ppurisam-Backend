package com.pprisam.backend.domain.ai.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.pprisam.backend.domain.ai.repository.ThemeEntity;
import com.pprisam.backend.domain.ai.repository.ThemeRepository;
import com.theokanning.openai.image.CreateImageRequest;
import com.theokanning.openai.image.ImageResult;
import com.theokanning.openai.service.OpenAiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.time.Duration;
import java.util.Optional;

@Slf4j
@Service
public class ImageAIService {

    private final OpenAiService openAiService;
    private final ThemeRepository themeRepository;
    private final TextAIService textAIService;

    @Autowired
    public ImageAIService(@Value("${openai.api.key}") String apiKey, ThemeRepository themeRepository, TextAIService textAIService) {
        this.openAiService = new OpenAiService(apiKey, Duration.ofSeconds(30));
        this.themeRepository = themeRepository;
        this.textAIService = textAIService;
    }

    // 생성된 이미지 URL만 반환
    public String generate(String prompt, String theme) throws JsonProcessingException {
        log.info("prompt : {}", prompt);

        String enPrompt = textAIService.translateKrPrompt(prompt);
        log.info("enPrompt : {}", enPrompt);

        ThemeEntity themeEntity = themeRepository.findByTheme(theme)
                .orElseThrow(() -> new NullPointerException("Not Found"));

        String newPrompt = String.format("""
            You are a graphic designer with 30 years of experience, dedicated to satisfying your clients.
            Your task is to create one image that perfectly matches the client's description.

            The tasks that need to be done are as follows :
            1) Analyze the client's description %s and create an image that considers this
            2) Please make an image by referring to the following theme : %s

            Remember : Text must not be included, and absolutely no letters or characters should appear in the image.
            
            """, enPrompt, themeEntity.getDescription());

        log.info("prompt : {}", newPrompt);

        // 요청 설정
        CreateImageRequest request = CreateImageRequest.builder()
                .prompt(newPrompt) // 프롬프트 설정
                .model("dall-e-3")  // DALL-E 3 모델 사용
                .n(1) // 생성할 이미지
                .build()
                ;

        // 이미지 생성 요청
        ImageResult response = openAiService.createImage(request);

        // 생성된 이미지 URL 반환
        return response.getData().getFirst().getUrl();
    }
}
