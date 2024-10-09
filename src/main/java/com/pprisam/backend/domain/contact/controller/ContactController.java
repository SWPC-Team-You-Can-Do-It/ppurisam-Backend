package com.pprisam.backend.domain.contact.controller;

import com.pprisam.backend.config.annotation.UserSession;
import com.pprisam.backend.domain.contact.model.ContactDTO;
import com.pprisam.backend.domain.contact.repository.ContactRepository;
import com.pprisam.backend.domain.contact.repository.entity.ContactEntity;
import com.pprisam.backend.domain.contact.service.ContactService;
import com.pprisam.backend.domain.user.model.User;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/open-api/contacts")   // jwt 관련 설정 세팅전 임시 endpoint 설정
public class ContactController {
    @Autowired
    private ContactService contactService;

    @PostMapping("")
    public ResponseEntity<ContactEntity> createContact(
            @RequestBody ContactDTO contactDTO
//            @Parameter(hidden = true)
//            @UserSession User user
    ) {

        ContactEntity savedContact = contactService.save(contactDTO);
        return ResponseEntity.ok(savedContact);

    }
}
