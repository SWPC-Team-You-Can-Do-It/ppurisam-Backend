package com.pprisam.backend.domain.contact.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;


@Data
public class ContactDTO {
    @NotBlank
    private String name;
    @NotBlank
    private String phoneNumber;

    private String memo;

}
