package com.pprisam.backend.domain.contact.repository;

import com.pprisam.backend.domain.contact.repository.entity.GroupEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupRepository extends JpaRepository<GroupEntity, Long> {
}
