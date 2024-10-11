package com.pprisam.backend.domain.contact.controller;

import com.pprisam.backend.config.annotation.UserSession;
import com.pprisam.backend.domain.contact.model.ContactDTO;
import com.pprisam.backend.domain.contact.repository.ContactRepository;
import com.pprisam.backend.domain.contact.service.ContactService;
import com.pprisam.backend.domain.user.model.User;
import com.pprisam.backend.domain.user.model.UserIdResponse;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contacts")
public class ContactController {
    @Autowired
    private ContactService contactService;
    @Autowired
    private ContactRepository contactRepository;

    @PostMapping("")
    public ResponseEntity<ContactDTO> createContact(
            @RequestBody ContactDTO contactDTO,
            @Parameter(hidden = true)
            @UserSession User user
    ){

        UserIdResponse userIdResponse = new UserIdResponse(user.getId());
        ContactDTO savedContact = contactService.save(contactDTO, userIdResponse);
        return ResponseEntity.ok(savedContact);
    }

    @GetMapping("")
    public ResponseEntity<List<ContactDTO>> getAllContacts(){
        List<ContactDTO> contacts = contactService.findAll(); // 전체 연락처 조회
        return ResponseEntity.ok(contacts);
    }
}
