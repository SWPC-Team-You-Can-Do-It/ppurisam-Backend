package com.pprisam.backend.domain.user.controller;

import com.pprisam.backend.domain.token.model.TokenResponse;
import com.pprisam.backend.domain.user.business.UserBusiness;
import com.pprisam.backend.domain.user.model.UserResponse;
import com.pprisam.backend.domain.user.model.UserLoginRequest;
import com.pprisam.backend.domain.user.model.UserRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/open-api/user")
public class UserOpenApiController {

    private final UserBusiness userBusiness;

    // 회원가입
    @PostMapping("/register")
    public UserResponse register(
            @Valid
            @RequestBody UserRequest userRequest
    ) {
        return userBusiness.register(userRequest);
    }

    // 로그인
    @PostMapping("/login")
    public TokenResponse login(
            @Valid
            @RequestBody UserLoginRequest userLoginRequest
    ) {
        return userBusiness.login(userLoginRequest);
    }
}
