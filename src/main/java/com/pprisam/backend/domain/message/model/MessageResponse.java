package com.pprisam.backend.domain.message.model;

import com.pprisam.backend.domain.image.model.ImageResponse;
import com.pprisam.backend.domain.ppurio.model.MessageFile;
import com.pprisam.backend.domain.ppurio.model.Target;
import com.pprisam.backend.domain.receiver.model.ReceiverResponse;
import com.pprisam.backend.domain.receiver.repository.ReceiverEntity;
import com.pprisam.backend.domain.user.repository.UserEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MessageResponse {

    private String title;

    private String content;

    private LocalDateTime sendAt;

    private boolean status;

    private String fromPhoneNumber;

    private List<ReceiverResponse> receivers;

    private List<ImageResponse> images;
}
