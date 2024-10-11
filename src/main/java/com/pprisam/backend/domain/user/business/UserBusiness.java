package com.pprisam.backend.domain.user.business;

import com.pprisam.backend.domain.token.business.TokenBusiness;
import com.pprisam.backend.domain.token.model.TokenResponse;
import com.pprisam.backend.domain.user.converter.UserConverter;
import com.pprisam.backend.domain.user.model.*;
import com.pprisam.backend.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserBusiness {

    private final UserService userService;
    private final UserConverter userConverter;
    private final TokenBusiness tokenBusiness;

    // 사용자 회원가입
    public UserResponse register(UserRequest request) {
        var userEntity=userConverter.toEntity(request); // 요청을 엔티티로 변환
        var newEntity=userService.register(userEntity); // 엔티티를 레포에 저장
        var userResponse=userConverter.toResponse(newEntity); // 저장한 값을 response로 변환

        return userResponse;
    }

    // 사용자 로그인
    public TokenResponse login(UserLoginRequest userLoginRequest) {
        var userEntity=userService.login(userLoginRequest.getEmail(), userLoginRequest.getPassword());

        return tokenBusiness.issueToken(userEntity);
    }

    // 사용자 정보 조회
    public UserResponse getUserInfo(User user) {
        var userEntity = userConverter.toEntity(user);
        var userResponse = userConverter.toResponse(userEntity);

        return userResponse;
    }

    // 사용자 정보 수정
    public UserResponse updateUserInfo(User user, UserUpdateRequest userUpdateRequest) {
        var updateEntity = userConverter.toEntity(user, userUpdateRequest);
        var newEntity = userService.update(updateEntity);
        var userResponse = userConverter.toResponse(newEntity);

        return userResponse;
    }

    // 사용자 탈퇴
    public UserResponse setUserInactive(User user) {
        var userEntity = userConverter.toEntity(user);
        var deleteEntity = userService.delete(userEntity);
        var userResponse = userConverter.toResponse(deleteEntity);

        return userResponse;
    }
}
