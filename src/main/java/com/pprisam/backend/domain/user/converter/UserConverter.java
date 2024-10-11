package com.pprisam.backend.domain.user.converter;

import com.pprisam.backend.domain.user.model.User;
import com.pprisam.backend.domain.user.model.UserResponse;
import com.pprisam.backend.domain.user.model.UserRequest;
import com.pprisam.backend.domain.user.model.UserUpdateRequest;
import com.pprisam.backend.domain.user.repository.UserEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserConverter {

    // 사용자 관련 요청을 엔티티로 변환
    public UserEntity toEntity(UserRequest userRequest) {
        return UserEntity.builder()
                .name(userRequest.getName())
                .email(userRequest.getEmail())
                .password(userRequest.getPassword())
                .phoneNumber(userRequest.getPhoneNumber())
                .build()
                ;
    }

    // Resolver에서 전달한 User 객체를 Entity로 변환
    public UserEntity toEntity(User user) {
        return UserEntity.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .password(user.getPassword())
                .phoneNumber(user.getPhoneNumber())
                .status(user.getStatus())
                .inactiveAt(user.getInactiveAt())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build()
                ;
    }

    // 사용자 정보 수정 시, Resolver에서 전달한 User 객체와 Request를 비교하여 Entity로 변환
    public UserEntity toEntity(User user, UserUpdateRequest userUpdateRequest) {
        // 이메일 업데이트
        Optional.ofNullable(userUpdateRequest.getEmail())
                .filter(email -> !email.equals(user.getEmail()))
                .ifPresent(email -> user.setEmail(email));

        // 비밀번호 업데이트
        Optional.ofNullable(userUpdateRequest.getPassword())
                .filter(password -> !password.equals(user.getPassword()))
                .ifPresent(password -> user.setPassword(password));

        // 이름 업데이트
        Optional.ofNullable(userUpdateRequest.getName())
                .filter(name -> !name.equals(user.getName()))
                .ifPresent(name -> user.setName(name));

        // 전화번호 업데이트
        Optional.ofNullable(userUpdateRequest.getPhoneNumber())
                .filter(phoneNumber -> !phoneNumber.equals(user.getPhoneNumber()))
                .ifPresent(phoneNumber -> user.setPhoneNumber(phoneNumber));

        // Request 반영한 User로 Entity 변환
        return toEntity(user);
    }

    // 응답을 내려주기 위해 엔티티를 Response로 변환
    public UserResponse toResponse(UserEntity userEntity) {
        return UserResponse.builder()
                .id(userEntity.getId())
                .name(userEntity.getName())
                .email(userEntity.getEmail())
                .password(userEntity.getPassword())
                .phoneNumber(userEntity.getPhoneNumber())
                .status(userEntity.getStatus())
                .inactiveAt(userEntity.getInactiveAt())
                .createdAt(userEntity.getCreatedAt())
                .updatedAt(userEntity.getUpdatedAt())
                .build()
                ;
    }
}
