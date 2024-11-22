package com.pprisam.backend.domain.message.repository;

import com.pprisam.backend.domain.message.repository.document.MessageDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface MessageDocumentRepository extends ElasticsearchRepository<MessageDocument, Long> {
    // 특정 사용자의 문자에 대해 제목 또는 내용으로 검색 (페이징 포함)
    Page<MessageDocument> findByUserIdAndTitleContainingOrContentContaining(Long userId, String title, String content, Pageable pageable);
}