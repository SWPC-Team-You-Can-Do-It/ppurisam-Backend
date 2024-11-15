package com.pprisam.backend.domain.receiver.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReceiverResponse {

    private String name;

    private String phoneNumber;
}
