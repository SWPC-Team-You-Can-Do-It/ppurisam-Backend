package com.pprisam.backend.domain.token.converter;

import com.pprisam.backend.domain.token.model.TokenDto;
import com.pprisam.backend.domain.token.model.TokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class TokenConverter {
    public TokenResponse toResponse(TokenDto accessToken, TokenDto refreshToken) {

        // Null 체크
        Objects.requireNonNull(accessToken, () -> {throw new RuntimeException("Access Token Null");});
        Objects.requireNonNull(refreshToken, () -> {throw new RuntimeException("Refresh Token Null");});

        // TokenResponse 생성
        return TokenResponse.builder()
                .accessToken(accessToken.getToken())
                .accessTokenExpiredAt(accessToken.getExpiredAt())
                .refreshToken(refreshToken.getToken())
                .refreshTokenExpiredAt(refreshToken.getExpiredAt())
                .build();
    }
}