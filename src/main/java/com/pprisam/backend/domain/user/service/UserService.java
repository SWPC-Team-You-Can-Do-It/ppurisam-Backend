package com.pprisam.backend.domain.user.service;

import com.pprisam.backend.domain.user.model.UserLoginRequest;
import com.pprisam.backend.domain.user.repository.UserEntity;
import com.pprisam.backend.domain.user.repository.UserRepository;
import com.pprisam.backend.domain.user.repository.enums.UserStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserEntity getUserWithThrow(Long userId) {
        return userRepository.findFirstByIdAndStatusOrderByIdDesc(userId, UserStatus.REGISTERED)
                .orElseThrow(()-> new NullPointerException("사용자를 찾을 수 없음"));
    }

    public UserEntity getUserWithThrow(String email, String password) {
        return userRepository.findFirstByEmailAndPasswordAndStatusOrderByIdDesc(email, password, UserStatus.REGISTERED)
                .orElseThrow(()->new NullPointerException("사용자를 찾을 수 없음"));
    }

    public UserEntity register(UserEntity userEntity) {
        return Optional.ofNullable(userEntity)
                .map(entity->{
                    entity.setStatus(UserStatus.REGISTERED);
                    entity.setCreatedAt(LocalDateTime.now());
                    entity.setUpdatedAt(LocalDateTime.now());
                    return userRepository.save(entity);
                })
                .orElseThrow(()->new NullPointerException("user null"));
    }

    public UserEntity login(String email, String password) {
        return getUserWithThrow(email, password);
    }

}
