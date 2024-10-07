package com.pprisam.backend;

import com.pprisam.backend.domain.token.helper.JwtTokenHelper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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
}
