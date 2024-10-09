package com.pprisam.backend.domain.contact.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;


@Data
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ContactDTO {

    private String name;

    private String phoneNumber;

    private String memo;

}
