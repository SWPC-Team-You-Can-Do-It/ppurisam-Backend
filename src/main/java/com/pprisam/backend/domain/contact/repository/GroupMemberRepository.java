package com.pprisam.backend.domain.contact.repository;

import com.pprisam.backend.domain.contact.repository.entity.GroupMemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GroupMemberRepository extends JpaRepository<GroupMemberEntity, Long> {

    // ContactEntity의 ID로 GroupMemberEntity를 찾는 메서드
    List<GroupMemberEntity> findByContactEntityId(Long contactId);
}
