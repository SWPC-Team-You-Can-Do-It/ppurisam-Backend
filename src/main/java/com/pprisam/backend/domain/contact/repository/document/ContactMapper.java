package com.pprisam.backend.domain.contact.repository.document;

import com.pprisam.backend.domain.contact.repository.entity.ContactEntity;
import com.pprisam.backend.domain.contact.repository.entity.GroupMemberEntity;
import com.pprisam.backend.domain.contact.repository.entity.GroupEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ContactMapper {

    public ContactDocument toDocument(ContactEntity entity, List<GroupMemberEntity> groupMembers) {
        if (entity == null) {
            return null;
        }

        List<ContactDocument.GroupInfo> groups = groupMembers.stream()
                .map(GroupMemberEntity::getGroupEntity)
                .map(this::mapGroup)
                .collect(Collectors.toList());

        return ContactDocument.builder()
                .id(entity.getId())
                .userId(entity.getUserEntity().getId())
                .name(entity.getName())
                .phoneNumber(entity.getPhoneNumber())
                .memo(entity.getMemo())
                .groups(groups)
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    private ContactDocument.GroupInfo mapGroup(GroupEntity groupEntity) {
        return ContactDocument.GroupInfo.builder()
                .id(groupEntity.getId())
                .name(groupEntity.getName())
                .build();
    }
}