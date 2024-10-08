package com.pprisam.backend.domain.user.repository;

import com.pprisam.backend.domain.user.repository.enums.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    // select * from user where id=? and status=? order by id desc limit 1;
    Optional<UserEntity> findFirstByIdAndStatusOrderByIdDesc(Long id, UserStatus status);

    //select * from user where email=? and password=? and status=? order by desc limit 1;
    Optional<UserEntity> findFirstByEmailAndPasswordAndStatusOrderByIdDesc(String email, String password, UserStatus status);
}
