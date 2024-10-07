package com.pprisam.backend.domain.contact.repository;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "group_member")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GroupMemberEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // id 기본키로 자동 생성
    private Long id;

    @ManyToOne
    @JoinColumn(name = "contact_id")
    private ContactEntity contactEntity;

    @ManyToOne
    @JoinColumn(name = "group_id")
    private GroupEntity groupEntity;
}
