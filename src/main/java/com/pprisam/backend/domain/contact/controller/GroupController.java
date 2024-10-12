package com.pprisam.backend.domain.contact.controller;

import com.pprisam.backend.domain.contact.model.GroupRequest;
import com.pprisam.backend.domain.contact.model.GroupResponse;
import com.pprisam.backend.domain.contact.repository.entity.GroupEntity;
import com.pprisam.backend.domain.contact.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/groups")
public class GroupController {

    @Autowired
    private GroupService groupService;

    @PostMapping("")   // 그룹 생성
    public ResponseEntity<GroupEntity> createGroup(@RequestBody GroupRequest groupRequest) {
        GroupEntity newGroup = groupService.createGroup(groupRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(newGroup);
    }

    @GetMapping("")     // 그룹 조회
    public ResponseEntity<List<GroupResponse>> getAllGroups() {
        List<GroupResponse> groups = groupService.findAll();
        return ResponseEntity.ok(groups);
    }

    @DeleteMapping("{id}")  // 그룹 삭제
    public ResponseEntity<GroupResponse> deleteGroup(@PathVariable Long id) {
        groupService.deleteGroup(id);
        return ResponseEntity.noContent().build();
    }

}
