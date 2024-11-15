package com.pprisam.backend.domain.receiver.converter;

import com.pprisam.backend.domain.receiver.model.ReceiverResponse;
import com.pprisam.backend.domain.receiver.repository.ReceiverEntity;
import org.springframework.stereotype.Service;


@Service
public class ReceiverConverter {

    public ReceiverResponse toResponse(ReceiverEntity receiverEntity) {

        return ReceiverResponse.builder()
                .name(receiverEntity.getName())
                .phoneNumber(receiverEntity.getPhoneNumber())
                .build()
                ;
    }
}
