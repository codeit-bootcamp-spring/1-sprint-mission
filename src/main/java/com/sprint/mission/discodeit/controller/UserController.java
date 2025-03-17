package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.docs.UserSwagger;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.data.UserStatusDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.mapper.UserStatusMapper;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import com.sprint.mission.discodeit.util.FileConverter;
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
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController implements UserSwagger {

  private final UserService userService;
  private final UserStatusService userStatusService;
  private final UserStatusMapper userStatusMapper;

  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<UserDto> create(
      @RequestPart("userCreateRequest") UserCreateRequest request,
      @RequestPart(value = "profile",
          required = false) MultipartFile profile
  ) {

    Optional<BinaryContentCreateRequest> profileRequest = Optional.ofNullable(profile)
        .map(FileConverter::resolveProfileRequest);
    User user = userService.create(request, profileRequest);

    UserDto userDto = userService.find(user.getId());

    return ResponseEntity.status(HttpStatus.CREATED)
        .body(userDto);
  }

  @PatchMapping(value = "/{userId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<UserDto> update(
      @PathVariable UUID userId,
      @RequestPart("userUpdateRequest") UserUpdateRequest request,
      @RequestPart(value = "profile",
          required = false) MultipartFile profile
  ) {

    Optional<BinaryContentCreateRequest> profileRequest = Optional.ofNullable(profile)
        .map(FileConverter::resolveProfileRequest);

    User user = userService.update(userId, request,
        profileRequest);

    UserDto userDto = userService.find(user.getId());
    return ResponseEntity.status(HttpStatus.OK)
        .body(userDto);

  }

  @PatchMapping(value = "/{userId}/userStatus")
  public ResponseEntity<UserStatusDto> updateUserStatus(
      @PathVariable UUID userId,
      @RequestBody UserStatusUpdateRequest request
  ) {
    UserStatus userStatus = userStatusService.updateByUserId(userId, request);
    return ResponseEntity.status(HttpStatus.OK)
        .body(userStatusMapper.toDto(userStatus));
  }

  @GetMapping
  public ResponseEntity<List<UserDto>> findAll() {
    List<UserDto> dtos = userService.findAll();
    return ResponseEntity.status(HttpStatus.OK)
        .body(dtos);
  }

  @DeleteMapping(value = "/{userId}")
  public ResponseEntity<Void> delete(@PathVariable UUID userId) {
    userService.delete(userId);
    return ResponseEntity.noContent().build();
  }
}
