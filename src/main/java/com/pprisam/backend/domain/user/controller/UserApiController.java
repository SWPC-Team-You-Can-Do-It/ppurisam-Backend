package com.pprisam.backend.domain.user.controller;

import com.pprisam.backend.config.annotation.UserSession;
import com.pprisam.backend.domain.user.business.UserBusiness;
import com.pprisam.backend.domain.user.model.User;
import com.pprisam.backend.domain.user.model.UserResponse;
import com.pprisam.backend.domain.user.model.UserUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserApiController {

    private static final Logger log = LoggerFactory.getLogger(UserApiController.class);
    private final UserBusiness userBusiness;

    // 사용자 정보 조회
    @GetMapping()
    public UserResponse getUserInfo(
            @UserSession User user
    ) {
        return userBusiness.getUserInfo(user);
    }

    // 사용자 정보 수정
    @PatchMapping()
    public UserResponse updateUserInfo(
            @UserSession User user,
            @RequestBody UserUpdateRequest userUpdateRequest
    ) {
        return userBusiness.updateUserInfo(user, userUpdateRequest);
    }

    // 사용자 탈퇴
    @DeleteMapping()
    public UserResponse deleteUser(
            @UserSession User user
    ) {
        return userBusiness.setUserInactive(user);
    }

}
