package com.pprisam.backend.domain.token.service;

import com.pprisam.backend.domain.token.helper.JwtTokenHelper;
import com.pprisam.backend.domain.token.model.TokenDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final JwtTokenHelper jwtTokenHelper;

    public TokenDto issueAccessToken(Long userId) {
        var data = new HashMap<String, Object>();
        data.put("userId", userId);

        // 액세스 토큰 발급
        return jwtTokenHelper.issueAccessToken(data);
    }

    public TokenDto issueRefreshToken(Long userId) {
        var data = new HashMap<String, Object>();
        data.put("userId", userId);

        // 리프레시 토큰 발급
        return jwtTokenHelper.issueRefreshToken(data);
    }

    public Long validationToken(String token) {
        // 토큰 검증 및 데이터 추출
        var map = jwtTokenHelper.validationTokenWithThrow(token);

        var userId = map.get("userId");

        // Null 체크
        Objects.requireNonNull(userId, () -> {throw new RuntimeException("UserId Null");});

        // userId 반환
        return Long.parseLong(userId.toString());
    }
}