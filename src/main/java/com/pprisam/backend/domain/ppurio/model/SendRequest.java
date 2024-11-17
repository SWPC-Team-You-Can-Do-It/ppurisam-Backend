package com.pprisam.backend.domain.ppurio.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.List;

@Data
public class SendRequest {
    private String account;

    @JsonProperty("messageType")
    private String messageType;

    private String content;
    private String from;

    private String title; // 추후 제목 추가를 위함

    @JsonProperty("duplicateFlag")
    private String duplicateFlag;

    @JsonProperty("targetCount")
    private int targetCount;

    private List<Target> targets;

    @JsonProperty("refKey")
    private String refKey;

    @JsonProperty("rejectType")
    private String rejectType;

    @JsonProperty("sendTime")
    private String sendTime;

    private String subject;

    private List<MessageFile> files;
}
