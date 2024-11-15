package com.pprisam.backend.domain.message.service;

import com.pprisam.backend.domain.message.converter.MessageConverter;
import com.pprisam.backend.domain.message.model.MessageResponse;

import com.pprisam.backend.domain.message.repository.MessageEntity;
import com.pprisam.backend.domain.message.repository.MessageRepository;
import com.pprisam.backend.domain.ppurio.model.SendRequest;
import com.pprisam.backend.domain.receiver.repository.ReceiverEntity;
import com.pprisam.backend.domain.receiver.repository.ReceiverRepository;
import com.pprisam.backend.domain.user.model.User;
import com.pprisam.backend.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class MessageService {

    private final MessageRepository messageRepository;
    private final ReceiverRepository receiverRepository;
    private final UserService userService;
    private final MessageConverter messageConverter;

    public MessageResponse saveMessage(Map<String, Object> params, User user, Boolean sendStatus) {

        String content = params.get("content") != null ? params.get("content").toString() : "";
        String sendTimeStr = params.get("sendTime") != null ? params.get("sendTime").toString() : LocalDateTime.now().toString();
        String title = params.get("title") != null ? params.get("title").toString() : "제목없음";
        String from = params.get("from") != null ? params.get("from").toString() : "";

        // sendTime 문자열을 LocalDateTime으로 변환
        LocalDateTime sendAt=toDateTime(sendTimeStr);


        // 유저 entity 가져오기
        var userEntity=userService.getUserWithThrow(user.getId());

        // MessageEntity 생성
        var messageEntity=MessageEntity.builder()
                .user(userEntity)
                .content(content)
                .title(title)
                .sendAt(sendAt)
                .status(sendStatus)
                .fromPhoneNumber(from)
                .build()
                ;

        // MessageEntity 저장
        var newMessageEntity=messageRepository.save(messageEntity);

        // target 리스트 가져오기
        List<Map<String, Object>> targets=(List<Map<String, Object>>) params.get("targets");

        // target 리스트 receiver로 변환 및 저장
        List<ReceiverEntity> receiverEntityList= targets.stream().map(target->{
                var entity=ReceiverEntity.builder()
                        .name(target.get("name").toString())
                        .phoneNumber(target.get("to").toString())
                        .message(newMessageEntity) // 다대일 연관관계
                        .build();
                var newEntity=receiverRepository.save(entity);

                log.info("저장된 ReceiverEntity: {}", newEntity);
                return newEntity;
            }
        ).toList();

        // TODO 이미지 처리 추가


        var messageResponse=messageConverter.toMessageResponse(messageEntity, receiverEntityList);

        return messageResponse;
    }

    public LocalDateTime toDateTime(String sendTimeStr) {
        LocalDateTime sendAt = null;
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS");
            sendAt = LocalDateTime.parse(sendTimeStr, formatter);
            log.info("시간 변환 성공");
            return sendAt;

        } catch (DateTimeParseException e) {
            sendAt = LocalDateTime.now(); // 변환 실패 시 현재 시간으로 설정
            log.info("시간 변환 실패 현재 시간으로 저장");
            return sendAt;
        }
    }
}
