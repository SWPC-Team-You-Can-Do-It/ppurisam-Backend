package com.pprisam.backend.domain.ppurio.controller;

import com.pprisam.backend.config.annotation.UserSession;
import com.pprisam.backend.domain.ppurio.model.SendRequest;
import com.pprisam.backend.domain.ppurio.model.TokenResponse;
import com.pprisam.backend.domain.ppurio.service.PpurioAuthService;
import com.pprisam.backend.domain.ppurio.service.PpurioService;
import com.pprisam.backend.domain.user.model.User;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/ppurio")
public class PpurioController {

    private final PpurioService ppurioService;
    private final PpurioAuthService ppurioAuthService;

    @PostMapping("/token")
    public ResponseEntity<TokenResponse> getToken() {
        TokenResponse tokenResponse = ppurioAuthService.getAccessToken();
        return ResponseEntity.ok(tokenResponse);
    }

    // MMS/SMS 발송 엔드포인트
    @PostMapping("/send")
    public ResponseEntity<?> send(@RequestBody SendRequest sendRequest, @Parameter(hidden = true) @UserSession User user) {
        System.out.println("Received SendRequest: " + sendRequest);
        ppurioService.send(sendRequest, user);
        return ResponseEntity.ok("MMS/SMS 발송이 성공적으로 완료되었습니다.");
    }
}
