package com.pprisam.backend.domain.ppurio.model;

import lombok.Data;

@Data
public class MessageFile {
    private String name;
    private Integer size;
    private String data;
    private String url;
}