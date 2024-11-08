package com.pprisam.backend.domain.ppurio.service;

import com.pprisam.backend.domain.ppurio.model.TokenResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class PpurioAuthService {

    private static final Logger logger = LoggerFactory.getLogger(PpurioAuthService.class);

    @Value("${ppurio.account}")
    private String account;

    @Value("${ppurio.api.key}") // 변경된 프로퍼티 이름
    private String authKey;

    @Value("${ppurio.uri}")
    private String uri;

    private final RestTemplate restTemplate;

    public PpurioAuthService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public TokenResponse getAccessToken() {
        logger.info("Obtaining access token with account: {}", account);
        // 실제 프로덕션 환경에서는 authKey를 로그에 남기지 않도록 주의하세요.
        // logger.debug("Ppurio Auth Key: {}", authKey);

        String credentials = account + ":" + authKey;
        String encodedCredentials = Base64.getEncoder().encodeToString(credentials.getBytes(StandardCharsets.UTF_8));

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Basic " + encodedCredentials);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<TokenResponse> response = restTemplate.exchange(
                    uri + "/v1/token",
                    HttpMethod.POST,
                    entity,
                    TokenResponse.class
            );

            if (response.getStatusCode() == HttpStatus.OK) {
                logger.info("Access token obtained successfully.");
                return response.getBody();
            } else {
                logger.error("Failed to obtain access token: {}", response.getStatusCode());
                throw new RuntimeException("토큰 발급 실패: " + response.getStatusCode());
            }
        } catch (Exception e) {
            logger.error("Exception while obtaining access token: ", e);
            throw new RuntimeException("토큰 발급 중 오류 발생: " + e.getMessage(), e);
        }
    }
}