package com.pprisam.backend.domain.message.repository;

import com.pprisam.backend.domain.ppurio.model.MessageFile;
import com.pprisam.backend.domain.receiver.repository.ReceiverEntity;
import com.pprisam.backend.domain.user.repository.UserEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "message")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class MessageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    // 시간은 SendRequest 에서 받아서 sendTime
    private LocalDateTime sendAt;

    // 성공, 실패
    @Column(nullable = false)
    private Boolean status;
    // 발신자 번호
    @Column(nullable = false)
    private String fromPhoneNumber;

    @OneToMany(mappedBy = "message")
    private List<ReceiverEntity> receivers;
}
