package com.pprisam.backend.domain.contact.service;

import com.pprisam.backend.domain.contact.model.ContactDTO;
import com.pprisam.backend.domain.contact.repository.ContactRepository;
import com.pprisam.backend.domain.contact.repository.entity.ContactEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ContactService {
    @Autowired
    private ContactRepository contactRepository;

    // DTO에서 데이터 출력
    public ContactEntity save(ContactDTO contactDTO) {
        ContactEntity contactEntity = ContactEntity.builder()
                .name(contactDTO.getName())
                .phoneNumber(contactDTO.getPhoneNumber())
                .memo(contactDTO.getMemo())
                .build();

        // Repository를 통해 DB에 저장
        return contactRepository.save(contactEntity);
    }

    public List<ContactEntity> findAll() {
        return contactRepository.findAll();
    }
}
