package com.pprisam.backend.domain.message.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MessageRepository extends JpaRepository<MessageEntity, Long> {
    // 페이징 고려해서 문자 Entity 조회
    @Query("SELECT m FROM MessageEntity m WHERE m.user.id = :userId ORDER BY m.sendAt DESC")
    Page<MessageEntity> findAllMessagesByUserId(Long userId, Pageable pageable);

    // 수신자 목록 Fetch Join해서 문자 조회
    @Query("SELECT m FROM MessageEntity m LEFT JOIN FETCH m.receivers WHERE m.id IN :ids ORDER BY m.sendAt DESC")
    List<MessageEntity> findMessagesWithReceiversByIds(List<Long> ids);

    // 이미지 Fetch Join해서 문자 조회
    @Query("SELECT m FROM MessageEntity m LEFT JOIN FETCH m.images WHERE m.id IN :ids ORDER BY m.sendAt DESC")
    List<MessageEntity> findMessagesWithImagesByIds(List<Long> ids);
}
