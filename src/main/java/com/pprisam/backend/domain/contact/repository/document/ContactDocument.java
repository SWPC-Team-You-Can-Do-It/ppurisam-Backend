package com.pprisam.backend.domain.contact.repository.document;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "contacts")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ContactDocument {

    @Id
    private Long id;

    @Field(type = FieldType.Long)
    private Long userId;

    @Field(type = FieldType.Long)
    private Long contactId;

    @Field(type = FieldType.Text, analyzer = "nori")
    private String name;

    @Field(type = FieldType.Text)
    private String phoneNumber;

    @Field(type = FieldType.Text)
    private String memo;

    @Field(type = FieldType.Nested, includeInParent = true)
    private List<GroupInfo> groups;

    @Field(type = FieldType.Date, format = DateFormat.date_time)
    private OffsetDateTime createdAt;

    @Field(type = FieldType.Date, format = DateFormat.date_time)
    private OffsetDateTime updatedAt;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GroupInfo {
        @Field(type = FieldType.Long)
        private Long id;

        @Field(type = FieldType.Text)
        private String name;
    }
}
