package com.pprisam.backend.domain.user.converter;

import com.pprisam.backend.domain.user.model.UserResponse;
import com.pprisam.backend.domain.user.model.UserRequest;
import com.pprisam.backend.domain.user.repository.UserEntity;
import org.springframework.stereotype.Service;

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
