package com.pprisam.backend.domain.contact.service;

import com.pprisam.backend.domain.contact.model.ContactRequest;
import com.pprisam.backend.domain.contact.model.ContactResponse;
import com.pprisam.backend.domain.contact.repository.ContactRepository;
import com.pprisam.backend.domain.contact.repository.entity.ContactEntity;
import com.pprisam.backend.domain.user.model.UserIdResponse;
import com.pprisam.backend.domain.user.repository.UserEntity;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ContactService {
    private final ContactRepository contactRepository;

    // DTO에서 데이터 출력
    public ContactResponse save(ContactRequest contactDTO, UserIdResponse userIdResponse) {
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

    private ContactResponse toResponse(ContactEntity contactEntity) {
        return ContactResponse.builder()
                .id(contactEntity.getId())
                .userId(contactEntity.getUserEntity().getId())  // userEntity의 id만 반환
                .name(contactEntity.getName())
                .phoneNumber(contactEntity.getPhoneNumber())
                .memo(contactEntity.getMemo())
                .build();
    }

    public List<ContactResponse> findAll() {
        return contactRepository.findAll().stream()
                .map(this::toResponse)  // ContactEntity를 ContactResponse로 변환
                .collect(Collectors.toList());
    }

    public ContactResponse findById(Long id) {
        return contactRepository.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 id: " + id)); // 예외 발생
    }
}
