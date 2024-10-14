package com.pprisam.backend.domain.contact.repository;

import com.pprisam.backend.domain.contact.repository.document.ContactDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContactDocumentRepository extends ElasticsearchRepository<ContactDocument, Long> {

    List<ContactDocument> findByNameContaining(String name);

    List<ContactDocument> findByPhoneNumber(String phoneNumber);

    // 그룹 이름으로 검색하는 메서드 예시
    List<ContactDocument> findByGroupsNameContaining(String groupName);
    void deleteByContactId(Long userId);

    List<ContactDocument> findByNameContainingOrPhoneNumberContaining(String name, String phoneNumber);
}