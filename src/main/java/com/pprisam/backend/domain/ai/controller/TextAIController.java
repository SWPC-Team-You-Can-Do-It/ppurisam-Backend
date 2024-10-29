package com.pprisam.backend.domain.ai.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.pprisam.backend.domain.ai.model.TextAIRequest;
import com.pprisam.backend.domain.ai.model.TextAIResponse;
import com.pprisam.backend.domain.ai.service.TextAIService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/text-ai")
public class TextAIController {

    private final TextAIService textAIService;

    @PostMapping("")
    public TextAIResponse generateText(@Valid @RequestBody TextAIRequest request) throws JsonProcessingException {

        return textAIService.generateText(request.getText());
    }
}
