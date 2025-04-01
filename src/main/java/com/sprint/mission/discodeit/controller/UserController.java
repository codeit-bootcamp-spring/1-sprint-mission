package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.api.UserApi;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.data.UserStatusDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.exception.binaryContent.FileProcessingException;
import com.sprint.mission.discodeit.service.basic.UserService;
import com.sprint.mission.discodeit.service.basic.UserStatusService;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/api/users")
public class UserController implements UserApi {

  private final UserService userService;
  private final UserStatusService userStatusService;

  @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
  @Override
  public ResponseEntity<UserDto> create(
      @RequestPart("userCreateRequest") UserCreateRequest userCreateRequest,
      @RequestPart(value = "profile", required = false) MultipartFile profile
  ) {
    log.debug("사용자 생성 요청 수신 - username: {}, email: {}", userCreateRequest.username(),
        userCreateRequest.email());
    Optional<BinaryContentCreateRequest> profileRequest = Optional.ofNullable(profile)
        .flatMap(this::resolveProfileRequest);
    try {
      UserDto createdUser = userService.create(userCreateRequest, profileRequest);
      log.info("사용자 생성 성공 - username: {}, email: {}", userCreateRequest.username(),
          userCreateRequest.email());
      return ResponseEntity
          .status(HttpStatus.CREATED)
          .body(createdUser);
    } catch (Exception e) {
      log.error("사용자 생성 실패 - username: {}, email: {}, 원인: {}", userCreateRequest.username(),
          userCreateRequest.email(), e.getMessage());
      return ResponseEntity
          .status(HttpStatus.INTERNAL_SERVER_ERROR)
          .build();
    }

  }

  @PatchMapping(
      path = "{userId}",
      consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}
  )
  @Override
  public ResponseEntity<UserDto> update(
      @PathVariable("userId") UUID userId,
      @RequestPart("userUpdateRequest") UserUpdateRequest userUpdateRequest,
      @RequestPart(value = "profile", required = false) MultipartFile profile
  ) {
    log.info("사용자 정보 수정 요청 수신 - userId: {}", userId);
    Optional<BinaryContentCreateRequest> profileRequest = Optional.ofNullable(profile)
        .flatMap(p -> {
          log.debug("프로필 첨부파일 수신 - fileName: {}, size: {}", p.getOriginalFilename(), p.getSize());
          return resolveProfileRequest(p);
        });

    try {
      UserDto updatedUser = userService.update(userId, userUpdateRequest, profileRequest);
      log.info("사용자 정보 수정 성공 - userId: {}", userId);
      return ResponseEntity
          .status(HttpStatus.OK)
          .body(updatedUser);
    } catch (Exception e) {
      log.error("사용자 정보 수정 실패 - userId: {}, 원인: {}", userId, e.getMessage());
      return ResponseEntity
          .status(HttpStatus.INTERNAL_SERVER_ERROR)
          .build();
    }
  }

  @DeleteMapping(path = "{userId}")
  @Override
  public ResponseEntity<Void> delete(@PathVariable("userId") UUID userId) {
    log.info("사용자 삭제 요청 수신 - userId: {}", userId);
    try {
      userService.delete(userId);
      log.info("사용자 삭제 성공 - userId: {}", userId);
      return ResponseEntity
          .status(HttpStatus.NO_CONTENT)
          .build();
    } catch (Exception e) {
      log.error("사용자 삭제 실패 - userId: {}, 원인: {}", userId, e.getMessage());
      return ResponseEntity
          .status(HttpStatus.INTERNAL_SERVER_ERROR)
          .build();
    }
  }

  @GetMapping
  @Override
  public ResponseEntity<List<UserDto>> findAll() {
    List<UserDto> users = userService.findAll();
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(users);
  }

  @PatchMapping(path = "{userId}/userStatus")
  @Override
  public ResponseEntity<UserStatusDto> updateUserStatusByUserId(@PathVariable("userId") UUID userId,
      @RequestBody UserStatusUpdateRequest request) {
    log.debug("사용자 상태 정보 수정 요청 수신 - userId: {}", userId);
    try {
      UserStatusDto updatedUserStatus = userStatusService.updateByUserId(userId, request);
      return ResponseEntity
          .status(HttpStatus.OK)
          .body(updatedUserStatus);
    } catch (Exception e) {
      log.error("사용자 상태 정보 수정 실패 - userId: {}, 원인: {}", userId, e.getMessage());
      return ResponseEntity
          .status(HttpStatus.INTERNAL_SERVER_ERROR)
          .build();
    }
  }

  private Optional<BinaryContentCreateRequest> resolveProfileRequest(MultipartFile profileFile) {
    if (profileFile.isEmpty()) {
      log.warn("첨부된 프로필 파일이 비어 있습니다 - fileName: {}", profileFile.getOriginalFilename());
      return Optional.empty();
    } else {
      try {
        BinaryContentCreateRequest binaryContentCreateRequest = new BinaryContentCreateRequest(
            profileFile.getOriginalFilename(),
            profileFile.getContentType(),
            profileFile.getBytes()
        );
        log.debug("프로필 파일 처리 완료 - fileName: {}, contentType: {}, size: {} bytes",
            profileFile.getOriginalFilename(),
            profileFile.getContentType(),
            profileFile.getSize());
        return Optional.of(binaryContentCreateRequest);
      } catch (IOException e) {
        log.error("프로필 파일 처리 실패 - fileName: {}, 원인: {}", profileFile.getOriginalFilename(),
            e.getMessage(), e);
        throw new FileProcessingException(profileFile.getOriginalFilename(), e);
      }
    }
  }
}
