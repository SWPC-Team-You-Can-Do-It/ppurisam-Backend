package com.pprisam.backend.domain.message.controller;

import com.pprisam.backend.config.annotation.UserSession;
import com.pprisam.backend.domain.message.model.MessagePageResponse;
import com.pprisam.backend.domain.message.model.MessageResponse;
import com.pprisam.backend.domain.message.repository.document.MessageDocument;
import com.pprisam.backend.domain.message.service.MessageESService;
import com.pprisam.backend.domain.message.service.MessageService;
import com.pprisam.backend.domain.ppurio.model.SendRequest;
import com.pprisam.backend.domain.ppurio.service.RequestService;
import com.pprisam.backend.domain.user.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/message")
@Slf4j
public class MessageController {

    private final MessageService messageService;
    private final MessageESService messageESService;
    private final RequestService requestService;

    // 문자 저장 endPoint 실제로 문자를 보내지 않음.
    // 테스트 용 실제로는 Ppurio 로직에서 관리
    @PostMapping("")
    public MessageResponse messageSend(@UserSession User user, @RequestBody SendRequest sendRequest) throws IOException {

        return messageService.saveMessage(sendRequest, user, true);
    }

    // 문자 조회
    @GetMapping("")
    public MessagePageResponse getMessages(
            @UserSession User user, @RequestParam(defaultValue = "0") int page) {
        Pageable pageable = PageRequest.of(page, 6);
        return messageService.findAll(user, pageable);
    }

    // 문자 검색
    @GetMapping("/search")
    public MessagePageResponse searchMessagesByTitleOrContent(@UserSession User user, @RequestParam String keyword, @RequestParam(defaultValue = "0") int page) {
        // 1) 엘라스틱 서치에선 해당되는 문자 ID 검색만 - 엘라스틱 서치에 관련 데이터 다 저장하는 것은 무리라고 판단
        // 2) 검색 결과를 통해 DB 재조회

        // 페이징
        Pageable pageable = PageRequest.of(page, 6);

        // 특정 사용자의 문자에 대해서 내용과 제목으로 검색(By 엘라스틱서치)
        Page<MessageDocument> searchResults = messageESService.searchMessages(user, keyword, pageable);

        // 검색 결과(ID)를 받아서 해당 결과에 대한 데이터 조회(from DB)
        return messageService.findAllSearchResult(pageable, searchResults);
    }
}