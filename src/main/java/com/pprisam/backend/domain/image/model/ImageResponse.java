package com.pprisam.backend.domain.image.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ImageResponse {

    private String name;
    private Integer size;
    private String url;
}
