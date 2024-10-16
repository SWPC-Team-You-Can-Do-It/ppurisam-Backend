package com.pprisam.backend.domain.contact.repository.entity;

import com.pprisam.backend.domain.user.repository.UserEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;

    private String name;

    private String phoneNumber;

    private String memo;

    @OneToMany(mappedBy = "contactEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GroupMemberEntity> groupMembers;

    @CreationTimestamp // 레코드가 생성될 때 자동으로 해당 필드에 현재 날짜와 시간을 기록
    private OffsetDateTime createdAt;

    @UpdateTimestamp // 레코드가 수정될 때 자동으로 해당 필드에 현재 날짜와 시간을 기록
    private OffsetDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        createdAt = OffsetDateTime.now();
        updatedAt = createdAt;
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = OffsetDateTime.now();
    }
}
