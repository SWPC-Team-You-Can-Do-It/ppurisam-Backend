package com.pprisam.backend.domain.ai.model;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TextAIEditRequest {

    @NotBlank
    private String textPre;

    @NotBlank
    private String textRefactor;
}
