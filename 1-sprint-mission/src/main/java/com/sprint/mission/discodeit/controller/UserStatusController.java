package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.request.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.request.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.dto.request.UserStatusCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.service.ReadStatusService;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/userStatues")
public class UserStatusController {

  private final UserStatusService userStatusService;

  @PostMapping
  public ResponseEntity<UserStatus> create(@RequestBody UserStatusCreateRequest request) {
    UserStatus createdUserStatus = userStatusService.create(request);
    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(createdUserStatus);
  }

  @PatchMapping(path = "{userStatusId}")
  public ResponseEntity<UserStatus> update(@PathVariable("userStatusId") UUID userStatusId,
      @RequestBody UserStatusUpdateRequest request) {
    UserStatus updateUserStatus = userStatusService.update(userStatusId, request);
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(updateUserStatus);
  }

  @GetMapping
  public ResponseEntity<UserStatus> findAllByUserId(@RequestParam("userId") UUID userId) {
    UserStatus userStatus = userStatusService.find(userId);
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(userStatus);
  }
}
