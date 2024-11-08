package com.pprisam.backend.domain.ppurio.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.Map;

@Data
public class Target {
    private String to;
    private String name;

    @JsonProperty("changeWord")
    private Map<String, String> changeWord;
}
