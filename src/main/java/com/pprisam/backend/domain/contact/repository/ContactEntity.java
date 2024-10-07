package com.pprisam.backend.domain.contact.repository;

import com.pprisam.backend.domain.user.repository.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "contact")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContactEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;

    private String name;

    private String phoneNumber;

    private String memo;

    @CreationTimestamp // 레코드가 생성될 때 자동으로 해당 필드에 현재 날짜와 시간을 기록
    private LocalDateTime createdAt;

    @UpdateTimestamp // 레코드가 수정될 때 자동으로 해당 필드에 현재 날짜와 시간을 기록
    private LocalDateTime updatedAt;
}
