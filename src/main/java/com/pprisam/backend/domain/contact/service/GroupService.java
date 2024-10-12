package com.pprisam.backend.domain.contact.service;

import com.pprisam.backend.domain.contact.model.GroupRequest;
import com.pprisam.backend.domain.contact.model.GroupResponse;
import com.pprisam.backend.domain.contact.repository.GroupRepository;
import com.pprisam.backend.domain.contact.repository.entity.GroupEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GroupService {
    private final GroupRepository groupRepository;

    // 그룹 생성
    public GroupEntity createGroup(GroupRequest groupRequest) {
        GroupEntity newGroup = GroupEntity.builder()
                .name(groupRequest.getName())
                .build();

        return groupRepository.save(newGroup); // 새 그룹을 저장
    }

    // 그룹 조회
    public List<GroupResponse> findAll() {
        return groupRepository.findAll().stream()
                .map(group -> new GroupResponse(
                        group.getId(),
                        group.getName(),
                        group.getCreatedAt(),
                        group.getUpdatedAt())) // 모든 필드 사용
                .toList();
    }

    // 그룹 삭제
    public void deleteGroup(Long id) {
        if (groupRepository.existsById(id)) { // 그룹이 존재하는지 확인
            groupRepository.deleteById(id); // 그룹 삭제
        } else {
            throw new IllegalArgumentException("그룹을 찾을 수 없습니다. ID: " + id);
        }
    }
}
