package com.pprisam.backend.domain.message.service;

import com.pprisam.backend.domain.message.repository.MessageDocumentRepository;
import com.pprisam.backend.domain.message.repository.document.MessageDocument;
import com.pprisam.backend.domain.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessageESService {

    private final MessageDocumentRepository messageDocumentRepository;

    // 엘라스틱 서치 저장
    public void save(MessageDocument messageDocument) {
        messageDocumentRepository.save(messageDocument);
    }

    // 엘라스틱 서치 조회 (제목 또는 내용에 대해)
    public Page<MessageDocument> searchMessages(User user, String titleOrContent, Pageable pageable) {
        return messageDocumentRepository.findByUserIdAndTitleContainingOrContentContaining(user.getId(), titleOrContent, titleOrContent, pageable);
    }
}