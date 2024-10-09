package com.pprisam.backend.domain.contact.repository;

import com.pprisam.backend.domain.contact.repository.entity.ContactEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactRepository extends JpaRepository<ContactEntity, Long> {
}
