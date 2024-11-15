package com.pprisam.backend.domain.message.controller;

import com.pprisam.backend.config.annotation.UserSession;
import com.pprisam.backend.domain.message.model.MessagePageResponse;
import com.pprisam.backend.domain.message.model.MessageResponse;
import com.pprisam.backend.domain.message.service.MessageService;
import com.pprisam.backend.domain.ppurio.model.SendRequest;
import com.pprisam.backend.domain.ppurio.service.RequestService;
import com.pprisam.backend.domain.user.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    private final RequestService requestService;

    // 문자 저장 endPoint 실제로 문자를 보내지 않음.
    // 테스트 용 실제로는 Ppurio 로직에서 관리
    @PostMapping("")
    public MessageResponse messageSend(@UserSession User user, @RequestBody SendRequest sendRequest) throws IOException {
        var params=requestService.createSendParams(sendRequest);
        log.info("MessageController parmas 값: {}", params);

        return messageService.saveMessage(params, user, true);
    }

    // 문자 조회
    @GetMapping("")
    public MessagePageResponse getMessages(
            @UserSession User user, @RequestParam(defaultValue = "0") int page) {
        Pageable pageable = PageRequest.of(page, 6);
        return messageService.findAll(user, pageable);
    }
}