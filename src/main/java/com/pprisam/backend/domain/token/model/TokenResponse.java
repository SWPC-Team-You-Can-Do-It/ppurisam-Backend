package com.pprisam.backend.domain.token.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TokenResponse {
    // 액세스 토큰
    private String accessToken;

    // 액세스 토큰의 만료시간
    private LocalDateTime accessTokenExpiredAt;

    // 새로운 액세스 토큰 발급에 사용되는 리프레시 토큰
    private String refreshToken;

    // 리프레시 토큰의 만료시간
    private LocalDateTime refreshTokenExpiredAt;
}