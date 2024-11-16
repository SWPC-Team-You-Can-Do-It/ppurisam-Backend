package com.pprisam.backend.domain.message.repository;

import com.pprisam.backend.domain.receiver.repository.ReceiverEntity;
import com.pprisam.backend.domain.user.repository.UserEntity;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
    private List<ReceiverEntity> receivers = new ArrayList<>();

    // 연관관계 편의 메소드
    public void addReceiver(ReceiverEntity receiver) {
        if (receivers == null) {
            receivers = new ArrayList<>();  //null 체크 안넣었더니 오류 발생해서 필요한 듯
        }
        if (!receivers.contains(receiver)) {  // 중복 방지
            receivers.add(receiver);
            receiver.setMessage(this); // 반대쪽 연관관계 설정
        }
    }
}
