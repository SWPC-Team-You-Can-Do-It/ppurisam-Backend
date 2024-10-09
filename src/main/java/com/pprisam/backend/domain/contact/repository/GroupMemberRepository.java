package com.pprisam.backend.domain.contact.repository;

import com.pprisam.backend.domain.contact.repository.entity.GroupMemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupMemberRepository extends JpaRepository<GroupMemberEntity, Long> {
}
