package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.swagger.UserApi;
import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserCreateDTO;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.dto.user.UserUpdateDTO;
import com.sprint.mission.discodeit.dto.userStatus.UserStatusDto;
import com.sprint.mission.discodeit.dto.userStatus.UserStatusUpdateDTO;
import com.sprint.mission.discodeit.service.UserService;
import jakarta.validation.Valid;
import java.io.IOException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController implements UserApi {

  private final UserService userService;

  @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
  public ResponseEntity<UserDto> create(
      @Valid @RequestPart("userCreateRequest") UserCreateDTO userCreateRequest,
      @RequestPart(value = "profile", required = false) MultipartFile profile
  ) {

    log.info("사용자 생성 요청 수신 - username: {}, email: {}",
        userCreateRequest.getUsername(), userCreateRequest.getEmail());

    Optional<BinaryContentCreateRequest> profileRequest = Optional.ofNullable(profile)
        .flatMap(this::resolveProfileRequest);
    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(userService.create(userCreateRequest, profileRequest));
  }

  @PreAuthorize("@accessManager.isSelfOrAdmin(#userId, authentication)")
  @PatchMapping("{userId}")
  public ResponseEntity<UserDto> update(@PathVariable UUID userId,
      @Valid @RequestPart("userUpdateDTO") UserUpdateDTO userUpdateRequest,
      @RequestPart(value = "profile", required = false) MultipartFile profile
  ) {

    log.info("사용자 생성 요청 수신 - newUsername: {}, newEmail: {}",
        userUpdateRequest.getNewUsername(), userUpdateRequest.getNewEmail());

    Optional<BinaryContentCreateRequest> profileRequest = Optional.ofNullable(profile)
        .flatMap(this::resolveProfileRequest);
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(userService.update(userId, userUpdateRequest, profileRequest));
  }

  @PreAuthorize("@accessManager.isSelfOrAdmin(#userId, authentication)")
  @DeleteMapping("{userId}")
  public ResponseEntity<Void> delete(@PathVariable UUID userId) {
    userService.delete(userId);
    return ResponseEntity
        .status(HttpStatus.NO_CONTENT)
        .build();
  }

  @GetMapping
  public ResponseEntity<List<UserDto>> findAll() {
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(userService.findAll());
  }


  private Optional<BinaryContentCreateRequest> resolveProfileRequest(MultipartFile profileFile) {
    if (profileFile.isEmpty()) {
      return Optional.empty();
    } else {
      try {
        BinaryContentCreateRequest binaryContentCreateRequest = new BinaryContentCreateRequest(
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
