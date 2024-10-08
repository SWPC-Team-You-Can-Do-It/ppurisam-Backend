package com.pprisam.backend.domain.token.business;

import com.pprisam.backend.domain.token.converter.TokenConverter;
import com.pprisam.backend.domain.token.model.TokenResponse;
import com.pprisam.backend.domain.token.service.TokenService;
import com.pprisam.backend.domain.user.repository.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TokenBusiness {

    private final TokenService tokenService;
    private final TokenConverter tokenConverter;

    // User 로그인 시, 토큰 발급 용도로 이용할 예정
    public TokenResponse issueToken(UserEntity userEntity) {
        return Optional.ofNullable(userEntity)
                // User Entity에서 User Id 추출
                .map(ue -> {
                    return ue.getId();
                })

                // User Id를 통해 JWT Access/Refresh Token 발급 + Converter를 사용하여 변환
                .map(userId -> {
                    var accessToken = tokenService.issueAccessToken(userId);
                    var refreshToken = tokenService.issueRefreshToken(userId);
                    return tokenConverter.toResponse(accessToken, refreshToken);
                })

                .orElseThrow(() -> new RuntimeException("Null"));
    }

    // 토큰 검증하여 User Id 반환
    public Long validationAccessToken(String accessToken) {
        var userId = tokenService.validationToken(accessToken);
        return userId;
    }

}
