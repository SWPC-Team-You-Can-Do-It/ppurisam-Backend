package com.pprisam.backend.domain.contact.service;

import com.pprisam.backend.domain.contact.model.ContactDTO;
import com.pprisam.backend.domain.contact.repository.ContactRepository;
import com.pprisam.backend.domain.contact.repository.entity.ContactEntity;
import com.pprisam.backend.domain.user.model.UserIdResponse;
import com.pprisam.backend.domain.user.repository.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ContactService {
    private final ContactRepository contactRepository;

    // DTO에서 데이터 출력
    public ContactDTO save(ContactDTO contactDTO, UserIdResponse userIdResponse) {
        UserEntity userEntity = UserEntity.builder()
                .id(userIdResponse.getId())
                .build();

        ContactEntity contactEntity = ContactEntity.builder()
                .name(contactDTO.getName())
                .phoneNumber(contactDTO.getPhoneNumber())
                .memo(contactDTO.getMemo())
                .userEntity(userEntity)
                .build();

        // DB에 저장된 ContactEntity
        ContactEntity savedContact = contactRepository.save(contactEntity);

        // toResponse를 사용해 ContactResponse로 변환 후 반환
        return toResponse(savedContact);
    }

    private ContactDTO toResponse(ContactEntity contactEntity) {
        return ContactDTO.builder()
                .userId(contactEntity.getUserEntity().getId())  // userEntity의 id만 반환
                .name(contactEntity.getName())
                .phoneNumber(contactEntity.getPhoneNumber())
                .memo(contactEntity.getMemo())
                .build();
    }

    public List<ContactDTO> findAll() {
        return contactRepository.findAll().stream()
                .map(this::toResponse)  // ContactEntity를 ContactResponse로 변환
                .collect(Collectors.toList());
    }
}
