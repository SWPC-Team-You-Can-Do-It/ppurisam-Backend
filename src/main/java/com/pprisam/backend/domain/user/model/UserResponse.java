package com.pprisam.backend.domain.user.model;

import com.pprisam.backend.domain.user.repository.enums.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {

    private Long id;

    private String name;

    private String email;

    private String password;

    private String phoneNumber;

    private UserStatus status;

    private LocalDateTime inactiveAt;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
