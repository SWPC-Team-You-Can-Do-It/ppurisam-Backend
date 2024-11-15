package com.pprisam.backend.domain.message.converter;

import com.pprisam.backend.domain.message.model.MessagePageResponse;
import com.pprisam.backend.domain.message.model.MessageResponse;
import com.pprisam.backend.domain.message.repository.MessageEntity;
import com.pprisam.backend.domain.receiver.converter.ReceiverConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MessageConverter {

    private final ReceiverConverter receiverConverter;

    public MessageResponse toMessageResponse(MessageEntity messageEntity) {

        var receivers=messageEntity.getReceivers().stream()
                        .map(entity->{
                            return receiverConverter.toResponse(entity);
                        }).toList();

        return MessageResponse.builder()
                .title(messageEntity.getTitle())
                .content(messageEntity.getContent())
                .sendAt(messageEntity.getSendAt())
                .status(messageEntity.getStatus())
                .fromPhoneNumber(messageEntity.getFromPhoneNumber())
                .receivers(receivers)
                .build()
                ;
    }

    // 문자 내용과 페이징 관련 정보들
    public MessagePageResponse toMessageResponsePage(
            List<MessageEntity> messagesWithReceivers, Pageable pageable, long totalElements) {

        // List<MessageEntity>를 List<MessageResponse>로 변환
        List<MessageResponse> messageResponses = messagesWithReceivers.stream()
                .map(msg -> toMessageResponse(msg))  // 변환 메서드 사용
                .collect(Collectors.toList());

        // 페이징 정보가 담긴 Response
        return MessagePageResponse.builder()
                .messages(messageResponses)
                .currentPage(pageable.getPageNumber())
                .pageSize(pageable.getPageSize())
                .totalPages((int) Math.ceil((double) totalElements / pageable.getPageSize()))
                .totalElements(totalElements)
                .build()
                ;
    }
}
