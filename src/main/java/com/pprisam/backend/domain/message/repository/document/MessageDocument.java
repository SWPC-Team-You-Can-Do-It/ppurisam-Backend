package com.pprisam.backend.domain.message.repository.document;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "messages")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MessageDocument {

    // 문자 ID
    @Id
    private Long id;

    // 제목
    @Field(type = FieldType.Text, analyzer = "nori")
    private String title;

    // 내용
    @Field(type = FieldType.Text, analyzer = "nori")
    private String content;

    // 사용자 ID
    @Field(type = FieldType.Long)
    private Long userId;
}
