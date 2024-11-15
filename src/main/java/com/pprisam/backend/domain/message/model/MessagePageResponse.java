package com.pprisam.backend.domain.message.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MessagePageResponse {
    // 문자
    private List<MessageResponse> messages;

    // 현재 페이지
    private int currentPage;

    // 개수
    private int pageSize;

    // 전체 페이지
    private int totalPages;

    // 전체 개수
    private long totalElements;
}
