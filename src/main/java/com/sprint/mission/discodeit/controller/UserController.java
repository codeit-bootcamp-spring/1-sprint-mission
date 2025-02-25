package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentCreateDto;
import com.sprint.mission.discodeit.dto.user.UserCreate;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.dto.user.UserUpdate;
import com.sprint.mission.discodeit.dto.user.userStatus.UserStatusUpdate;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserController {

  private final UserService userService;
  private final UserStatusService userStatusService;

  @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
  public ResponseEntity<User> createUser(
      @RequestPart("userCreate") UserCreate userCreate,
      @RequestPart(value = "profile", required = false) MultipartFile profile
  ) throws IOException {
    Optional<BinaryContentCreateDto> profileRequest = processProfile(profile);
    User createdUser = userService.createUser(userCreate, profileRequest);
    return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
  }

  @PutMapping(value = "{userId}", consumes = {"multipart/form-data"})
  public ResponseEntity<User> updateUser(@PathVariable UUID userId,
      @RequestPart("userUpdate") UserUpdate userUpdate,
      @RequestPart(value = "profile", required = false) MultipartFile profile) throws IOException {
    Optional<BinaryContentCreateDto> profileRequest = processProfile(profile);
    User updatedUser = userService.update(userId, userUpdate, profileRequest);
    return ResponseEntity.ok(updatedUser);
  }

  @DeleteMapping("{userId}")
  public ResponseEntity<Void> deleteUser(@PathVariable UUID userId) {
    userService.delete(userId);
    return ResponseEntity.noContent().build();
  }

  @GetMapping
  public ResponseEntity<List<UserDto>> findAll() {
    List<UserDto> users = userService.findAll();
    return ResponseEntity.ok(users);
  }

  @PatchMapping("{userId}/status")
  public ResponseEntity<UserStatus> updateUserStatusByUserId(@PathVariable UUID userId,
      @RequestBody UserStatusUpdate userStatusUpdate) {
    UserStatus updatedUserStatus = userStatusService.updateByUserId(userId, userStatusUpdate);
    return ResponseEntity.ok(updatedUserStatus);
  }

  private Optional<BinaryContentCreateDto> processProfile(MultipartFile multipartFile)
      throws IOException {
    if (multipartFile == null || multipartFile.isEmpty()) {
      return Optional.empty();
    }
    try {
      return Optional.of(new BinaryContentCreateDto(
          multipartFile.getOriginalFilename(),
          multipartFile.getContentType(),
          multipartFile.getBytes()
      ));
    } catch (IOException e) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "유효하지 않은 파일입니다.", e);
    }
  }
}
