package com.pprisam.backend.domain.image.repository;

import com.pprisam.backend.domain.message.repository.MessageEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "image")
public class ImageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 긴 길이에 대비 TEXT type 설정
    @Column(columnDefinition = "TEXT")
    private String url;

    private String name;

    private Integer size;

    @ManyToOne
    @JoinColumn(name = "message_id")
    private MessageEntity message;
}
