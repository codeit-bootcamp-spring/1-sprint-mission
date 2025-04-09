package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.dto.UserStatusDto;
import com.sprint.mission.discodeit.dto.UsersDto;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.dto.request.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.dto.response.ApiResponse;
import com.sprint.mission.discodeit.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

  private final UserService userService;

  @Operation(summary = "회원 목록 조회", description = "전체 회원 조회")
  @GetMapping
  public ResponseEntity<List<UserDto>> listUsers() {
    return ResponseEntity.ok(userService.findAll());
  }

  @Operation(summary = "회원 상세 조회", description = "단일 회원 조회")
  @GetMapping("/{userId}")
  public ResponseEntity<UserDto> getUser(@PathVariable UUID userId) {
    return ResponseEntity.ok(userService.find(userId));
  }

  @Operation(summary = "회원 가입", description = "회원 가입")
  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<UserDto> registerUser(@Valid
      @RequestPart("userCreateRequest") UserCreateRequest userCreateRequest,
      @RequestPart(value = "profile", required = false) MultipartFile profile) throws IOException {
    log.info("[회원가입 요청] userCreateRequest: name={}, email={}, password={}",
        userCreateRequest.getUsername(), userCreateRequest.getEmail(),
        userCreateRequest.getPassword());

    UserDto userDTO = UserDto.builder()
        .name(userCreateRequest.getUsername())
        .email(userCreateRequest.getEmail())
        .password(userCreateRequest.getPassword())
        .build();

    UserDto createdUser = userService.createWithProfileImage(userDTO, profile);
    return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
  }

  @Operation(summary = "회원 정보 수정", description = "회원 정보 수정")
  @PatchMapping(value = "/{userId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<UserDto> updateUser(
      @PathVariable UUID userId,
      @RequestPart("userUpdateRequest") UserUpdateRequest userUpdateRequest,
      @RequestPart(value = "profile", required = false) MultipartFile profile) throws IOException {

    UserDto updatedUser = userService.update(userId, userUpdateRequest, profile);
    return ResponseEntity.ok(updatedUser);
  }

  @Operation(summary = "유저 삭제", description = "회원 정보 삭제")
  @DeleteMapping("/{userId}")
  public ResponseEntity<Void> deleteUser(@PathVariable UUID userId) {
    userService.delete(userId);
    return ResponseEntity.noContent().build();
  }

  @Operation(summary = "상태 업데이트", description = "사용자의 온라인 상태 업데이트")
  @PatchMapping("/{userId}/userStatus")
  public ResponseEntity<UserStatusDto> updateUserStatus(
      @PathVariable UUID userId,
      @RequestBody UserStatusUpdateRequest userStatusUpdateRequest) {

    UserStatusDto updatedStatus = userService.updateUserStatus(userId, userStatusUpdateRequest);
    return ResponseEntity.ok(updatedStatus);
  }
}
