package com.pprisam.backend.domain.contact.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GroupResponse {
    private Long id;

    private String name;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
