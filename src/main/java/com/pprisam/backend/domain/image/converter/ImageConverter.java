package com.pprisam.backend.domain.image.converter;

import com.pprisam.backend.domain.image.model.ImageResponse;
import com.pprisam.backend.domain.image.repository.ImageEntity;
import com.pprisam.backend.domain.receiver.model.ReceiverResponse;
import com.pprisam.backend.domain.receiver.repository.ReceiverEntity;
import org.springframework.stereotype.Service;

@Service
public class ImageConverter {

    public ImageResponse toResponse(ImageEntity imageEntity) {

        return ImageResponse.builder()
                .name(imageEntity.getName())
                .size(imageEntity.getSize())
                .url(imageEntity.getUrl())
                .build()
                ;
    }
}
