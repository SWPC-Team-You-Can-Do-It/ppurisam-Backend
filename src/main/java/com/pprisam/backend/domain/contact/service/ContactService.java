package com.pprisam.backend.domain.contact.service;

import com.pprisam.backend.domain.contact.model.ContactRequest;
import com.pprisam.backend.domain.contact.model.ContactResponse;
import com.pprisam.backend.domain.contact.repository.*;
import com.pprisam.backend.domain.contact.repository.document.ContactDocument;
import com.pprisam.backend.domain.contact.repository.document.ContactMapper;
import com.pprisam.backend.domain.contact.repository.entity.ContactEntity;
import com.pprisam.backend.domain.contact.repository.entity.GroupEntity;
import com.pprisam.backend.domain.contact.repository.entity.GroupMemberEntity;
import com.pprisam.backend.domain.user.model.UserIdResponse;
import com.pprisam.backend.domain.user.repository.UserEntity;
import com.pprisam.backend.domain.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ContactService {
    private final ContactRepository contactRepository;
    private final GroupRepository groupRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final ContactDocumentRepository contactDocumentRepository;
    private final ContactMapper contactMapper;

    // contactRequest에서 입력한 정보를 바탕으로 contact 데이터를 저장하고 groupMember에 함께 저장
    public ContactResponse save(ContactRequest contactRequest, UserIdResponse userIdResponse) {
        // 인증된 user_id 사용
        UserEntity userEntity = UserEntity.builder()
                .id(userIdResponse.getId())
                .build();

        // ContactEntity 생성
        ContactEntity contactEntity = ContactEntity.builder()
                .name(contactRequest.getName())
                .phoneNumber(contactRequest.getPhoneNumber())
                .memo(contactRequest.getMemo())
                .userEntity(userEntity)
                .build();

        // ContactEntity에 저장
        ContactEntity savedContact = contactRepository.save(contactEntity);

        // GroupEntity 조회
        GroupEntity groupEntity = groupRepository.findById(contactRequest.getGroupId())
                .orElseThrow(() -> new EntityNotFoundException("그룹을 찾을 수 없습니다. ID: " + contactRequest.getGroupId()));

        // GroupMember에 데이터 저장
        GroupMemberEntity groupMemberEntity = GroupMemberEntity.builder()
                .contactEntity(savedContact)
                .groupEntity(groupEntity)
                .build();
        groupMemberRepository.save(groupMemberEntity);

        // Elasticsearch 도큐먼트 생성 및 저장
        List<GroupMemberEntity> groupMembers = groupMemberRepository.findByContactEntityId(savedContact.getId());
        ContactDocument contactDocument = contactMapper.toDocument(savedContact, groupMembers);
        contactDocumentRepository.save(contactDocument);

        // toResponse를 사용해 ContactResponse로 변환 후 반환
        return toResponse(savedContact);
    }

    @Transactional
    public ContactResponse update(Long contactId, ContactRequest contactRequest, UserIdResponse userIdResponse) {
        // 인증된 user_id 사용
        UserEntity userEntity = UserEntity.builder()
                .id(userIdResponse.getId())
                .build();

        // 기존 ContactEntity 조회
        ContactEntity existingContact = contactRepository.findById(contactId)
                .orElseThrow(() -> new EntityNotFoundException("연락처를 찾을 수 없습니다. ID: " + contactId));

        // ContactEntity 업데이트
        existingContact.setName(contactRequest.getName());
        existingContact.setPhoneNumber(contactRequest.getPhoneNumber());
        existingContact.setMemo(contactRequest.getMemo());
        existingContact.setUserEntity(userEntity);
        contactRepository.save(existingContact);

        // 기존 GroupMemberEntity 조회 (List에서 첫 번째 요소를 가져옴)
        GroupMemberEntity existingGroupMember = groupMemberRepository.findByContactEntityId(existingContact.getId())
                .stream()
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("그룹 멤버를 찾을 수 없습니다. 연락처 ID: " + contactId));

        // 새로운 GroupEntity 조회
        GroupEntity newGroupEntity = groupRepository.findById(contactRequest.getGroupId())
                .orElseThrow(() -> new EntityNotFoundException("그룹을 찾을 수 없습니다. ID: " + contactRequest.getGroupId()));

        // GroupMemberEntity 업데이트
        existingGroupMember.setGroupEntity(newGroupEntity);
        groupMemberRepository.save(existingGroupMember);

        // Elasticsearch 도큐먼트 업데이트
        List<GroupMemberEntity> groupMembers = groupMemberRepository.findByContactEntityId(existingContact.getId());
        ContactDocument contactDocument = contactMapper.toDocument(existingContact, groupMembers);
        contactDocumentRepository.save(contactDocument);

        // toResponse를 사용해 ContactResponse로 변환 후 반환
        return toResponse(existingContact);
    }

    // 새로 추가된 delete 메서드
    @Transactional
    public void delete(Long contactId, UserIdResponse userIdResponse) {
        // 기존 ContactEntity 조회
        ContactEntity contactEntity = contactRepository.findById(contactId)
                .orElseThrow(() -> new EntityNotFoundException("연락처를 찾을 수 없습니다. ID: " + contactId));

        // GroupMemberEntity 조회
        List<GroupMemberEntity> groupMembers = groupMemberRepository.findByContactEntityId(contactId);

        // GroupMemberEntity 삭제
        if (!groupMembers.isEmpty()) {
            groupMemberRepository.deleteAll(groupMembers);
        }

        // ContactEntity 삭제
        contactRepository.delete(contactEntity);

        // Elasticsearch 도큐먼트 삭제
        contactDocumentRepository.deleteById(contactId);
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

    // contact 데이터 전체 조회
    public List<ContactResponse> findAll() {
        return contactRepository.findAll().stream()
                .map(this::toResponse)  // ContactEntity를 ContactResponse로 변환
                .collect(Collectors.toList());
    }

    // contact 데이터 세부 조회
    public ContactResponse findById(Long id) {
        return contactRepository.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 id: " + id)); // 예외 발생
    }
}
