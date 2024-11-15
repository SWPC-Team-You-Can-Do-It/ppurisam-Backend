package com.pprisam.backend.domain.ai.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pprisam.backend.domain.ai.model.TextAIResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class TextAIService {

    @Value("${openai.api.key}")
    private String openAiApiKey;

    private final RestTemplate restTemplate = new RestTemplate();
    private final String apiUrl = "https://api.openai.com/v1/chat/completions";

    public TextAIResponse generateText(String inputText) throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + openAiApiKey);
        headers.set("Content-Type", "application/json");

        // Prompt 생성
        String prompt = "You are a high-quality text message generation service. " +
                "Based on the text provided below, please create a well-crafted message with appropriate length. " +
                "Make sure the content of the message is clear and concise, and the flow of the sentences is natural. " +
                "Include additional details if necessary to make the message more informative and appealing. " +
                "Use a respectful tone when writing the message, and highlight any important information that should stand out. " +
                "Please ensure the message is at least 150 characters long and does not exceed 1000 characters in length. " +
                "Under no circumstances should you ask any clarifying questions or seek additional information regarding this request. " +
                "Generate the message based on the following text: \"" + inputText + "\" " +
                "Please respond in Korean.";

        // model 지정
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "gpt-4o-mini");

        // 메시지 배열 추가 (system 역할 지정 가능)
        requestBody.put("messages", new Object[] {
                new HashMap<String, String>() {{
                    put("role", "user");
                    put("content", prompt);
                }}
        });
        requestBody.put("max_tokens", 500);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

        // API 호출
        ResponseEntity<String> response = restTemplate.exchange(apiUrl, HttpMethod.POST, requestEntity, String.class);

        // JSON 데이터 가져오기
        var responseBody=response.getBody();
        log.info("ResponseBody: {}", responseBody);

        // Jackson ObjectMapper를 사용하여 JSON 파싱
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(responseBody);

        var generatedText=root.path("choices").get(0).path("message").path("content").asText();

        return TextAIResponse.builder()
                .generatedText(generatedText)
                .build()
                ;
    }

    public TextAIResponse editText(String textPre, String textRefactor) throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + openAiApiKey);
        headers.set("Content-Type", "application/json");

        // 수정 요청 프롬프트 생성
        String prompt = "You are an advanced text message revision service. " +
                "The user has provided feedback on a previously generated message and would like some modifications. " +
                "Based on the original message and the feedback provided, please revise the message while maintaining clarity, " +
                "conciseness, and a natural flow. Ensure that the revised message does not exceed 1000 characters and is at least 150 characters long. " +
                "Please use a respectful tone and implement any highlighted requirements from the user feedback. " +
                "Under no circumstances should you ask any clarifying questions or seek additional information regarding this request. " +
                "Original message: \"" + textPre + "\" " +
                "User feedback: \"" + textRefactor + "\" " +
                "Respond in Korean.";

        // model 지정
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "gpt-4o-mini");

        // 메시지 배열 추가 (system 역할 지정 가능)
        requestBody.put("messages", new Object[]{
                new HashMap<String, String>() {{
                    put("role", "user");
                    put("content", prompt);
                }}
        });
        requestBody.put("max_tokens", 500);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

        // API 호출
        ResponseEntity<String> response = restTemplate.exchange(apiUrl, HttpMethod.POST, requestEntity, String.class);

        // JSON 데이터 가져오기
        var responseBody = response.getBody();
        log.info("ResponseBody: {}", responseBody);

        // Jackson ObjectMapper를 사용하여 JSON 파싱
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(responseBody);

        var modifiedText = root.path("choices").get(0).path("message").path("content").asText();

        return TextAIResponse.builder()
                .generatedText(modifiedText)
                .build();
    }


    public String translateKrPrompt(String krPrompt) throws JsonProcessingException {
        // 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + openAiApiKey);
        headers.set("Content-Type", "application/json");

        // 메시지 리스트 생성
        List<Map<String, String>> messages = new ArrayList<>();
        messages.add(Map.of("role", "system", "content", "You are a translator. Translate the following Korean text to English."));
        messages.add(Map.of("role", "user", "content", krPrompt));

        // 바디 설정
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "gpt-4o-mini");
        requestBody.put("messages", messages);
        requestBody.put("max_tokens", 500);
        requestBody.put("temperature", 0.0);

        // 요청 엔티티 생성
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

        // API 호출
        ResponseEntity<String> response = restTemplate.exchange(apiUrl, HttpMethod.POST, requestEntity, String.class);

        // JSON 응답 파싱
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(response.getBody());

        // 필요한 부분 추출
        String translatedPrompt = root.path("choices").get(0).path("message").path("content").asText().trim();

        return translatedPrompt;
    }
}
