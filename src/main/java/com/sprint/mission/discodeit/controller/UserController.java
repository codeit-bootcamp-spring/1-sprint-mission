package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentRequest;
import com.sprint.mission.discodeit.dto.user.UserCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserResponse;
import com.sprint.mission.discodeit.dto.user.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

  private final UserService userService;

  @PostMapping
  public ResponseEntity<User> create(@RequestPart UserCreateRequest userCreateRequest,
      @RequestPart(required = false) MultipartFile multipartFile) {
    Optional<BinaryContentRequest> binaryContentRequest = Optional.ofNullable(multipartFile)
        .flatMap(this::resolveProfileRequest);

    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(userService.create(userCreateRequest, binaryContentRequest));
  }

  @GetMapping("/{id}")
  public ResponseEntity<UserResponse> getUserById(@PathVariable UUID id) {
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(userService.find(id));
  }

  @GetMapping
  public ResponseEntity<List<UserResponse>> getUsers() {
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(userService.findAll());
  }

  @PutMapping("/{id}")
  public ResponseEntity<User> update(@PathVariable UUID id,
      @RequestPart UserUpdateRequest userUpdateRequest,
      @RequestPart(required = false) MultipartFile multipartFile) {
    Optional<BinaryContentRequest> binaryContentRequest = Optional.ofNullable(multipartFile)
        .flatMap(this::resolveProfileRequest);

    return ResponseEntity
        .status(HttpStatus.OK)
        .body(userService.update(id, userUpdateRequest, binaryContentRequest));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable UUID id) {
    userService.delete(id);
    return ResponseEntity
        .status(HttpStatus.NO_CONTENT)
        .build();
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
