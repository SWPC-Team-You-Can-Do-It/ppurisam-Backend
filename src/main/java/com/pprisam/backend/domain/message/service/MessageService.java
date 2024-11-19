package com.pprisam.backend.domain.message.service;

import com.pprisam.backend.domain.image.repository.ImageEntity;
import com.pprisam.backend.domain.image.repository.ImageRepository;
import com.pprisam.backend.domain.message.converter.MessageConverter;
import com.pprisam.backend.domain.message.model.MessagePageResponse;
import com.pprisam.backend.domain.message.model.MessageResponse;
import com.pprisam.backend.domain.message.repository.MessageEntity;
import com.pprisam.backend.domain.message.repository.MessageRepository;
import com.pprisam.backend.domain.ppurio.model.SendRequest;
import com.pprisam.backend.domain.ppurio.model.Target;
import com.pprisam.backend.domain.receiver.repository.ReceiverEntity;
import com.pprisam.backend.domain.receiver.repository.ReceiverRepository;
import com.pprisam.backend.domain.user.model.User;
import com.pprisam.backend.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
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
    private final ImageRepository imageRepository;

    public MessageResponse saveMessage(SendRequest sendRequest, User user, Boolean sendStatus) {

        String content = sendRequest.getContent();
        String sendTimeStr = sendRequest.getSendTime() != null ? sendRequest.getSendTime() : LocalDateTime.now().toString();
        String title =  sendRequest.getTitle() !=null ? sendRequest.getTitle() : "제목없음";
        String from = sendRequest.getFrom() != null ? sendRequest.getFrom() : "";

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

        // target 리스트 가져오기
        List<Target> targets=sendRequest.getTargets();

        // target 리스트 receiver로 변환 및 저장
        List<ReceiverEntity> receiverEntityList= targets.stream().map(target->{
                var receiver=ReceiverEntity.builder()
                        .name(target.getName())
                        .phoneNumber(target.getTo())
                        .build();

                messageEntity.addReceiver(receiver); // 양방향 연관관계 설정

                return receiver;
            }
        ).toList();


        // 이미지 파일 처리: 파일이 있는 경우만 처리
        if (sendRequest.getFiles() != null && !sendRequest.getFiles().isEmpty()) {
            var file = sendRequest.getFiles().get(0); // 첫 번째 파일 가져오기

            var imageEntity = ImageEntity.builder()
                    .url(file.getUrl())
                    .size(file.getSize())
                    .name(file.getName())
                    .build();
            messageEntity.addImage(imageEntity); // 양방향 연관관계 설정
        }


        MessageEntity savedMessage = messageRepository.save(messageEntity); //문자 저장
        receiverRepository.saveAll(receiverEntityList); //수신자 저장

        if (messageEntity.getImages() != null && !messageEntity.getImages().isEmpty()) {
            imageRepository.saveAll(messageEntity.getImages()); // 이미지 저장
        }

        var messageResponse=messageConverter.toMessageResponse(savedMessage);

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

    // 문자 목록 조회
    public MessagePageResponse findAll(User user, Pageable pageable) {
        // 페이징 고려한 사용자의 문자Entity 리스트
        Page<MessageEntity> messageList = messageRepository.findAllMessagesByUserId(user.getId(), pageable);

        // 문자Entity리스트에서 문자 ID만 추출
        List<Long> messageIds = messageList.getContent().stream()
            .map((MessageEntity::getId))
            .collect(Collectors.toList())
            ;

        // 문자ID로 다시 문자 조회 (N+1문제로 인해 수신자 리스트 Fetch Join)
        List<MessageEntity> messagesWithReceivers = messageRepository.findMessagesWithReceiversByIds(messageIds);

        // 문자ID로 다시 문자 조회 (N+1문제로 인해 이미지 Fetch Join)
        List<MessageEntity> messagesWithImages = messageRepository.findMessagesWithImagesByIds(messageIds);

        // 결과 합치기
        List<MessageEntity> combineMessages = combineMessage(messagesWithReceivers, messagesWithImages);

        // 결과 반환
        return messageConverter.toMessageResponsePage(
                combineMessages, pageable, messageList.getTotalElements()
        );
    }

    // 수신자 포함한 문자 리스트와 이미지 포함한 문자 리스트 병합
    private List<MessageEntity> combineMessage(List<MessageEntity> messagesWithReceivers, List<MessageEntity> messagesWithImages) {
        // 병합하기 위한 임시
        Map<Long, MessageEntity> messageMap = new LinkedHashMap<>();

        // 수신자 정보를 가진 메시지들을 맵에 추가
        for (MessageEntity message : messagesWithReceivers) {
            messageMap.put(message.getId(), message);
        }

        // 수신자 메시지에 이미지 정보 추가
        for (MessageEntity message : messagesWithImages) {
            if(message.getImages()!=null && !message.getImages().isEmpty()) {
                MessageEntity existingMessage = messageMap.get(message.getId());
                existingMessage.setImages(message.getImages());
            }
        }

        // 맵의 값들을 리스트로 변환하여 반환
        return new ArrayList<>(messageMap.values());
    }
}
