package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.api.UserApi;
import com.sprint.mission.discodeit.dto.user.UserCreateRequestDto;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.dto.user.UserUpdateRequestDto;
import com.sprint.mission.discodeit.dto.userstatus.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.service.Interface.UserService;
import com.sprint.mission.discodeit.service.Interface.UserStatusService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController implements UserApi {

  private final UserService userService;
  private final UserStatusService userStatusService;

  @Override
  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<UserDto> createUser(
      @Valid @RequestPart("userCreateRequest") UserCreateRequestDto userCreateRequest,
      @RequestPart(value = "profile", required = false) MultipartFile profile) {

    UserDto createdUser = userService.createUser(userCreateRequest, profile);
    return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
  }

  @Override
  @GetMapping
  public ResponseEntity<List<UserDto>> findAll() {
    List<UserDto> userDto = userService.getAllUsers();
    return ResponseEntity.ok(userDto);
  }

  @Override
  @PatchMapping(value = "/{userId}", consumes = {
      MediaType.MULTIPART_FORM_DATA_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<UserDto> updateUser(
      @PathVariable("userId") UUID userId,
      @Valid @RequestPart("userUpdateRequest") UserUpdateRequestDto userUpdateRequest,
      @RequestPart(value = "profile", required = false) MultipartFile profile
  ) {

    UserDto updatedUser = userService.updateUser(userId, userUpdateRequest, profile);
    return ResponseEntity.ok(updatedUser);
  }

  @Override
  @DeleteMapping("/{userId}")
  public ResponseEntity<Void> deleteUser(@PathVariable UUID userId) {
    userService.deleteUser(userId);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

  @Override
  @PatchMapping(value = "/{userId}/userStatus", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<UserStatus> updateUserStatusByUserId(@PathVariable("userId") UUID userId,
      @RequestBody
      UserStatusUpdateRequest request) {
    UserStatus userStatus = userStatusService.updateByUserId(userId, request);
    return ResponseEntity.ok(userStatus);
  }
}
