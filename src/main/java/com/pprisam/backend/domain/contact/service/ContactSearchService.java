package com.pprisam.backend.domain.contact.service;

import com.pprisam.backend.domain.contact.repository.ContactDocumentRepository;
import com.pprisam.backend.domain.contact.repository.document.ContactDocument;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ContactSearchService {

    private final ContactDocumentRepository contactDocumentRepository;

    public List<ContactDocument> searchContacts(String nameOrPhoneNumber) {
        return contactDocumentRepository.findByNameContainingOrPhoneNumberContaining(nameOrPhoneNumber, nameOrPhoneNumber);
    }
}