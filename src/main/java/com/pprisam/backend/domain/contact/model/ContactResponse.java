package com.pprisam.backend.domain.contact.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContactResponse {
    private Long id;

    private Long userId;

    private String name;

    private String phoneNumber;

    private String memo;
}
