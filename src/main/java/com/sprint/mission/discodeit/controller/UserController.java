package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentRequest;
import com.sprint.mission.discodeit.dto.user.UserCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserResponse;
import com.sprint.mission.discodeit.dto.user.UserUpdateRequest;
import com.sprint.mission.discodeit.dto.userStatus.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {
  private final UserService userService;
  private final UserStatusService userStatusService;

  @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
  public ResponseEntity<User> create(@RequestPart UserCreateRequest userCreateRequest,
      @RequestPart(value = "profile", required = false) MultipartFile profile) {
    Optional<BinaryContentRequest> binaryContentRequest = Optional.ofNullable(profile)
        .flatMap(this::resolveProfileRequest);

    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(userService.create(userCreateRequest, binaryContentRequest));
  }

  @PatchMapping(value = "/{userId}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
  public ResponseEntity<User> update(@PathVariable UUID userId,
      @RequestPart UserUpdateRequest userUpdateRequest,
      @RequestPart(required = false) MultipartFile profile) {
    Optional<BinaryContentRequest> binaryContentRequest = Optional.ofNullable(profile)
        .flatMap(this::resolveProfileRequest);

    return ResponseEntity
        .status(HttpStatus.OK)
        .body(userService.update(userId, userUpdateRequest, binaryContentRequest));
  }

  @DeleteMapping("/{userId}")
  public ResponseEntity<Void> delete(@PathVariable UUID userId) {
    userService.delete(userId);
    return ResponseEntity
        .status(HttpStatus.NO_CONTENT)
        .build();
  }

  @GetMapping
  public ResponseEntity<List<UserResponse>> findAll() {
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(userService.findAll());
  }

  @PatchMapping("/{userId}/userStatus")
  public ResponseEntity<UserStatus> updateUserStatusByUserId(@PathVariable UUID userId,
      @RequestBody UserStatusUpdateRequest request) {
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(userStatusService.updateByUserUd(userId, request));
  }

  @GetMapping("/{id}")
  public ResponseEntity<UserResponse> getUserById(@PathVariable UUID id) {
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(userService.find(id));
  }

  private Optional<BinaryContentRequest> resolveProfileRequest(MultipartFile profileFile) {
    if (profileFile.isEmpty()) {
      return Optional.empty();
    } else {
      try {
        BinaryContentRequest binaryContentCreateRequest = new BinaryContentRequest(
            profileFile.getOriginalFilename(),
            profileFile.getContentType(),
            profileFile.getBytes()
        );
        return Optional.of(binaryContentCreateRequest);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
  }
}
