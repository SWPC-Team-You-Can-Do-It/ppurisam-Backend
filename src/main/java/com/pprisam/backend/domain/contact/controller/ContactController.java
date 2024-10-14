package com.pprisam.backend.domain.contact.controller;

import com.pprisam.backend.config.annotation.UserSession;
import com.pprisam.backend.domain.contact.model.ContactRequest;
import com.pprisam.backend.domain.contact.model.ContactResponse;
import com.pprisam.backend.domain.contact.repository.document.ContactDocument;
import com.pprisam.backend.domain.contact.service.ContactSearchService;
import com.pprisam.backend.domain.contact.service.ContactService;
import com.pprisam.backend.domain.user.model.User;
import com.pprisam.backend.domain.user.model.UserIdResponse;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.info.Contact;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/contacts")
public class ContactController {
    @Autowired
    private ContactService contactService;

    @Autowired
    private ContactSearchService contactSearchService;

    @PostMapping("")
    public ResponseEntity<ContactResponse> createContact(
            @RequestBody ContactRequest contactDTO,
            @Parameter(hidden = true)
            @UserSession User user
    ){

        UserIdResponse userIdResponse = new UserIdResponse(user.getId());
        ContactResponse savedContact = contactService.save(contactDTO, userIdResponse);
        return ResponseEntity.ok(savedContact);
    }

    // 업데이트 메소드
    @PutMapping("/{id}")
    public ResponseEntity<ContactResponse> updateContact(
            @PathVariable("id") Long contactId,
            @RequestBody ContactRequest contactDTO,
            @Parameter(hidden = true)
            @UserSession User user
    ){
        UserIdResponse userIdResponse = new UserIdResponse(user.getId());
        ContactResponse updatedContact = contactService.update(contactId, contactDTO, userIdResponse);
        return ResponseEntity.ok(updatedContact);
    }

    // 삭제 메소드
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContact(
            @PathVariable("id") Long contactId,
            @Parameter(hidden = true)
            @UserSession User user
    ) {
        UserIdResponse userIdResponse = new UserIdResponse(user.getId());
        contactService.delete(contactId, userIdResponse);
        return ResponseEntity.noContent().build(); // 204 No Content 반환
    }

    @GetMapping("")
    public ResponseEntity<List<ContactResponse>> getAllContacts(){
        List<ContactResponse> contacts = contactService.findAll(); // 전체 연락처 조회
        return ResponseEntity.ok(contacts);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ContactResponse> getContactById(@PathVariable Long id){
        return ResponseEntity.ok(contactService.findById(id));
    }

    @GetMapping("/search")
    public ResponseEntity<List<ContactDocument>> searchContactsByNameOrPhoneNumber(@RequestParam String keyword) {
        try {
            List<ContactDocument> results = contactSearchService.searchContacts(keyword);
            return ResponseEntity.ok(results);
        } catch (Exception e) {
            // 여기서 구체적인 에러 로그를 추가
            e.printStackTrace();
            return ResponseEntity.status(500).body(Collections.emptyList());
        }
    }
}
