package com.pprisam.backend.domain.message.converter;

import com.pprisam.backend.domain.message.model.MessageResponse;
import com.pprisam.backend.domain.message.repository.MessageEntity;
import com.pprisam.backend.domain.receiver.converter.ReceiverConverter;
import com.pprisam.backend.domain.receiver.repository.ReceiverEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageConverter {

    private final ReceiverConverter receiverConverter;

    public MessageResponse toMessageResponse(MessageEntity messageEntity , List<ReceiverEntity> receiverEntityList) {

        var receivers=receiverEntityList.stream()
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
}
