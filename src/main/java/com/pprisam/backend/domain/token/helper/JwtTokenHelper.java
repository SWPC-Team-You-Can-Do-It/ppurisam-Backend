package com.pprisam.backend.domain.token.helper;

import com.pprisam.backend.domain.token.model.TokenDto;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Setter
@Component
@Slf4j
public class JwtTokenHelper {

    @Value("${token.secret.key}")
    private String secretKey;
    @Value("${token.accessToken.plusHour}")
    private Long accessTokenPlusHour;
    @Value("${token.refreshToken.plusHour}")
    private Long refreshTokenPlusHour;

    public TokenDto issueAccessToken(Map<String, Object> data) {

        // 만료될 시간 = 현재시간 + 토큰 지속시간
        LocalDateTime expiredLocalDateTime=LocalDateTime.now().plusHours(accessTokenPlusHour);
        // jwt 토큰을 만들기 위해서 LocalDateTime -> Date 타입으로 변환
        Date expiredAt=Date.from(expiredLocalDateTime.atZone(ZoneId.systemDefault()).toInstant());
        // jwt 토큰이 위조되었는지 판단할 비밀키 생성 (해시 기반 알고리즘에 사용)
        SecretKey key= Keys.hmacShaKeyFor(secretKey.getBytes());

        String jwtToken= Jwts.builder() // 토큰 생성
                .signWith(key, SignatureAlgorithm.HS256) // 서명 생성
                .setClaims(data) // 토큰에 넣을 사용자 데이터에 해당(페이로드)
                .setExpiration(expiredAt) // jwt 의 만료시간을 설정
                .compact();

        return TokenDto.builder() // 리턴할 토큰 정보 객체 생성
                .token(jwtToken)
                .expiredAt(expiredLocalDateTime)
                .build();
    }

    // 토큰 재발급에 사용될 메서드
    public TokenDto issueRefreshToken(Map<String, Object> data) {

        // 만료될 시간 = 현재시간 + 토큰 지속시간
        LocalDateTime expiredLocalDateTime=LocalDateTime.now().plusHours(refreshTokenPlusHour);
        // jwt 토큰을 만들기 위해서 LocalDateTime -> Date 타입으로 변환
        Date expiredAt=Date.from(expiredLocalDateTime.atZone(ZoneId.systemDefault()).toInstant());
        // jwt 토큰이 위조되었는지 판단할 비밀키 생성 (해시 기반 알고리즘에 사용)
        SecretKey key= Keys.hmacShaKeyFor(secretKey.getBytes());

        String jwtToken= Jwts.builder() // 토큰 생성
                .signWith(key, SignatureAlgorithm.HS256) // 서명 생성
                .setClaims(data) // 토큰에 넣을 사용자 데이터에 해당(페이로드)
                .setExpiration(expiredAt) // jwt 의 만료시간을 설정
                .compact();

        return TokenDto.builder() // 리턴할 토큰 정보 객체 생성
                .token(jwtToken)
                .expiredAt(expiredLocalDateTime)
                .build();
    }

    // 토큰 검증에 사용될 메서드
    public Map<String, Object> validationTokenWithThrow(String token) {
        var key=Keys.hmacShaKeyFor(secretKey.getBytes());

        var parser=Jwts.parser() // 서명 검증 및 페이로드를 가져오기 위한 parser
                .setSigningKey(key)
                .build();

        try{
            var result = parser.parseClaimsJws(token); // 검증, 페이로드 가져오기
            log.info("토큰 문자열 파싱 결과: {}", result);

            return new HashMap<String, Object>(result.getBody());

        }catch (Exception e){

            if(e instanceof SignatureException){
                // 토큰이 유효하지 않을때
                throw new RuntimeException("에러 발생");
            }
            else if(e instanceof ExpiredJwtException){
                //  만료된 토큰
                throw new RuntimeException("에러 발생");
            }
            else{
                // 그외 에러
                throw new RuntimeException("에러 발생");
            }
        }
    }
}
