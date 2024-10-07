package com.pprisam.backend;

import com.pprisam.backend.domain.token.converter.TokenConverter;
import com.pprisam.backend.domain.token.helper.JwtTokenHelper;
import com.pprisam.backend.domain.token.model.TokenDto;
import com.pprisam.backend.domain.token.model.TokenResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.HashMap;
import static org.junit.jupiter.api.Assertions.*;

public class TokenTest {

    @Test
    void 토큰검증() {
        //given
        var userId=1;

        var data=new HashMap<String, Object>();
        data.put("userId", userId);

        // JwtTokenHelper 생성 및 설정
        // 테스트시 필드 설정을 위해서 JwtTokenHelper에 Setter 어노테이션 필요
        JwtTokenHelper jwtTokenHelper=new JwtTokenHelper();
        jwtTokenHelper.setSecretKey("ppurisamisabestservicehahahaha!@#");
        jwtTokenHelper.setAccessTokenPlusHour(1L);
        jwtTokenHelper.setRefreshTokenPlusHour(12L);

        //when
        var tokenDto=jwtTokenHelper.issueAccessToken(data);
        var resultMap=jwtTokenHelper.validationTokenWithThrow(tokenDto.getToken());

        //then
        assertEquals(userId, resultMap.get("userId"));
    }

    @Test
    void convert() {
        // given
        TokenConverter tokenConverter = new TokenConverter();

        TokenDto accessToken = new TokenDto("accessTokenValue", LocalDateTime.now().plusMinutes(10));
        TokenDto refreshToken = new TokenDto("refreshTokenValue", LocalDateTime.now().plusDays(10));

        // when
        TokenResponse response = tokenConverter.toResponse(accessToken, refreshToken);

        // then
        assertNotNull(response);

        System.out.println("[Response - accessToken] : " + response.getAccessToken() + " / " + response.getAccessTokenExpiredAt());
        assertEquals("accessTokenValue", response.getAccessToken());
        assertEquals(accessToken.getExpiredAt(), response.getAccessTokenExpiredAt());

        System.out.println("[Response - refreshToken] : " + response.getRefreshToken() + " / " + response.getRefreshTokenExpiredAt());
        assertEquals("refreshTokenValue", response.getRefreshToken());
        assertEquals(refreshToken.getExpiredAt(), response.getRefreshTokenExpiredAt());
    }
}
