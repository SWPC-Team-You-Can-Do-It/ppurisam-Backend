package com.pprisam.backend.domain.ai.model;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImageAIRequest {
    @NonNull
    private String prompt;

    @NonNull
    private String theme;
}
