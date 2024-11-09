package com.pprisam.backend.domain.contact.repository.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "contact_group")  // group이 예약어로 테이블명 생성 불가하여 대체
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GroupEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // id 기본키로 자동 생성
    private Long id;

    @NotNull
    private String name;

    @OneToMany(mappedBy = "groupEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ContactEntity> contacts = new ArrayList<>();

    @CreationTimestamp // 레코드가 생성될 때 자동으로 해당 필드에 현재 날짜와 시간을 기록
    private LocalDateTime createdAt;

    @UpdateTimestamp // 레코드가 수정될 때 자동으로 해당 필드에 현재 날짜와 시간을 기록
    private LocalDateTime updatedAt;
}
