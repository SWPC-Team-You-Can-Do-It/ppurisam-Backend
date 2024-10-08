package com.pprisam.backend.domain.user.service;

import com.pprisam.backend.domain.user.repository.UserEntity;
import com.pprisam.backend.domain.user.repository.UserRepository;
import com.pprisam.backend.domain.user.repository.enums.UserStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserEntity getUserWithThrow(Long userId) {
        return userRepository.findFirstByIdAndStatusOrderByIdDesc(userId, UserStatus.REGISTERED)
                .orElseThrow(()-> new NullPointerException("사용자를 찾을 수 없음"));
    }

}
