package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.api.UserApi;
import com.sprint.mission.discodeit.dto.binarycontetnt.CreateBinaryContentRequest;
import com.sprint.mission.discodeit.dto.status.UpdateUserStatusRequest;
import com.sprint.mission.discodeit.dto.status.UserStatusResponse;
import com.sprint.mission.discodeit.dto.user.CreateUserRequest;
import com.sprint.mission.discodeit.dto.user.UpdateUserRequest;
import com.sprint.mission.discodeit.dto.user.UserResponse;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController implements UserApi {

  private final UserService userService;
  private final UserStatusService userStatusService;

  @Override
  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<UserResponse> createUser(
      @RequestPart("user") CreateUserRequest request,
      @RequestPart(value = "file", required = false) MultipartFile file
  ) throws
      IOException {

    CreateBinaryContentRequest binaryRequest = file != null ? new CreateBinaryContentRequest(
        file.getOriginalFilename(),
        file.getContentType(),
        file.getBytes()) : null;

    UserResponse userResponse = userService.createUser(
        request,
        Optional.ofNullable(binaryRequest));
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(userResponse);
  }

  @Override
  @GetMapping("/{id}")
  public ResponseEntity<UserResponse> getUserById(@PathVariable UUID id) {
    return userService.findUserById(id)
        .map(ResponseEntity::ok)
        .orElseGet(
            () -> ResponseEntity.notFound()
                .build());
  }

  @Override
  @GetMapping
  public ResponseEntity<List<UserResponse>> getAllUsers() {
    List<UserResponse> users = userService.findAllUsers();
    return ResponseEntity.ok(users);
  }

  @Override
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteUser(@PathVariable UUID id) {
    userService.deleteUser(id);
    return ResponseEntity.noContent()
        .build();
  }

  @Override
  @PatchMapping(value = "{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<UserResponse> updateUser(
      @PathVariable UUID id,
      @RequestPart("user") UpdateUserRequest request,
      @RequestPart(value = "file", required = false) MultipartFile file
  ) throws
      IOException {

    CreateBinaryContentRequest binaryRequest = file != null ? new CreateBinaryContentRequest(
        file.getOriginalFilename(),
        file.getContentType(),
        file.getBytes()) : null;

    return userService.updateUser(
            id,
            request,
            Optional.ofNullable(binaryRequest))
        .map(
            ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound()
            .build());
  }

  @Override
  @PatchMapping("/{userId}/userStatus")
  public ResponseEntity<UserStatusResponse> updateUserUserStatus(
      @PathVariable UUID userId,
      @RequestBody UpdateUserStatusRequest request
  ) {
    return ResponseEntity.ok(userStatusService.update(
        userId,
        request));
  }
}
