package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.user.UserCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.dto.user.UserUpdateRequest;
import com.sprint.mission.discodeit.dto.user_status.UserStatusDto;
import com.sprint.mission.discodeit.dto.user_status.UserStatusUpdateRequest;

import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;
  private final UserStatusService userStatusService;

  @PostMapping(value = "", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
  public ResponseEntity<UserDto> createUser(
      @Valid @RequestPart("userCreateRequest") UserCreateRequest userCreateRequest,
      @RequestPart(value = "profile", required = false) MultipartFile profile
  ) {
    log.info("사용자 생성 요청: {}", userCreateRequest);
    UserDto creatUser = userService.create(userCreateRequest, profile);
    log.debug("사용자 생성 응답: {}", creatUser);
    return ResponseEntity.status(HttpStatus.CREATED).body(creatUser);
  }

  @PutMapping(value = "/{userId}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
  public ResponseEntity<UserDto> updateUser(
      @PathVariable("userId") UUID userId,
      @Valid @RequestPart("userUpdateRequest") UserUpdateRequest userUpdateRequest,
      @RequestPart(value = "profile", required = false) MultipartFile profile
  ) {
    log.info("사용자 수정 요청: {}", userUpdateRequest);
    UserDto updateUser = userService.update(userId, userUpdateRequest, profile);
    log.debug("사용자 수정 응답: {}", updateUser);
    return ResponseEntity.status(HttpStatus.OK).body(updateUser);
  }

  @DeleteMapping(value = "/{userId}")
  public ResponseEntity<Void> deleteUser(@PathVariable("userId") UUID userId) {
    log.info("사용자 삭제 요청: id={}", userId);
    userService.delete(userId);
    log.debug("사용자 삭제 완료");
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

  @GetMapping(value = "")
  public ResponseEntity<List<UserDto>> getAllUsers() {
    log.info("사용자 목록 조회 요청");
    List<UserDto> userList = userService.findAll();
    log.debug("사용자 목록 조회 응답: count={}", userList.size());
    return ResponseEntity.status(HttpStatus.OK).body(userList);
  }

  @PutMapping(value = "/{userId}/status")
  public ResponseEntity<UserStatusDto> updateUserStatus(
      @PathVariable("userId") UUID userId,
      @Valid @RequestBody UserStatusUpdateRequest request) {
    log.info("사용자 상태 수정 요청: id={}, request={}", userId, request);
    UserStatusDto updateUserStatus = userStatusService.updateByUserId(userId, request);
    log.debug("사용자 상태 수정 응답: {}", updateUserStatus);
    return ResponseEntity.status(HttpStatus.OK).body(updateUserStatus);
  }
}
