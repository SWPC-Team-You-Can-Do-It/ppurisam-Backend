package com.pprisam.backend.domain.image.model;

import lombok.Data;

@Data
public class ImageResponse {

    private String name;
    private long size;

    public ImageResponse(String name, long size) {
        this.name = name;
        this.size = size;
    }

}
