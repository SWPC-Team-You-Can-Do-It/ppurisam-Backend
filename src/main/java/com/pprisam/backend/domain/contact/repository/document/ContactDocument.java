package com.pprisam.backend.domain.contact.repository.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "contacts")
public class ContactDocument {

    @org.springframework.data.annotation.Id
    private Long id;

    private Long userId;

    private Long contactId;

    private String name;

    private String phoneNumber;

    private String memo;

    @Field(type = FieldType.Nested, includeInParent = true)
    private List<GroupInfo> groups;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GroupInfo {
        private Long id;
        private String name;
    }
}
