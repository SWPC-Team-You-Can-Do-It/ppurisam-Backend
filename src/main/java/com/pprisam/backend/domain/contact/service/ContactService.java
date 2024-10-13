package com.pprisam.backend.domain.contact.service;

import com.pprisam.backend.domain.contact.model.ContactRequest;
import com.pprisam.backend.domain.contact.model.ContactResponse;
import com.pprisam.backend.domain.contact.repository.ContactRepository;
import com.pprisam.backend.domain.contact.repository.GroupMemberRepository;
import com.pprisam.backend.domain.contact.repository.GroupRepository;
import com.pprisam.backend.domain.contact.repository.entity.ContactEntity;
import com.pprisam.backend.domain.contact.repository.entity.GroupEntity;
import com.pprisam.backend.domain.contact.repository.entity.GroupMemberEntity;
import com.pprisam.backend.domain.user.model.UserIdResponse;
import com.pprisam.backend.domain.user.repository.UserEntity;
import com.pprisam.backend.domain.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ContactService {
    private final ContactRepository contactRepository;
    private final UserRepository userRepository;
    private final GroupRepository groupRepository;
    private final GroupMemberRepository groupMemberRepository;

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
