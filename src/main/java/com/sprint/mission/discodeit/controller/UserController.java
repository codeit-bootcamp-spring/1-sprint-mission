package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.api.UserApi;
import com.sprint.mission.discodeit.converter.MultipartFileToBinaryContentConverter;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.data.UserStatusDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
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

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserController implements UserApi {

  private final UserService userService;
  private final UserStatusService userStatusService;
  private final MultipartFileToBinaryContentConverter fileConverter;

  @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
  @Override
  public ResponseEntity<UserDto> create(
      @RequestPart("userCreateRequest") @Valid UserCreateRequest userCreateRequest,
      @RequestPart(value = "profile", required = false) MultipartFile profile
  ) {
    log.info("POST /api/users - 사용자 생성 요청: {}", userCreateRequest.username());
    if (profile != null) {
      log.debug("프로필 파일 첨부됨: {}", userCreateRequest.username());
    }

    Optional<BinaryContentCreateRequest> profileRequest = Optional.ofNullable(profile)
        .map(fileConverter::convert);

    UserDto userDto = userService.create(userCreateRequest, profileRequest);
    log.info("사용자 생성 완료 - ID: {}", userDto.id());
    return ResponseEntity.status(HttpStatus.CREATED).body(userDto);

  }

  @PatchMapping(
      path = "{userId}",
      consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}
  )
  @Override
  public ResponseEntity<UserDto> update(
      @PathVariable("userId") UUID userId,
      @RequestPart("userUpdateRequest") @Valid UserUpdateRequest userUpdateRequest,
      @RequestPart(value = "profile", required = false) MultipartFile profile
  ) {
    log.info("PATCH /api/users/{} - 사용자 정보 수정 요청", userId);
    if (profile != null) {
      log.debug("프로필 업데이트 파일 첨부됨: {}", userId);
    }

    BinaryContentCreateRequest profileRequest = fileConverter.convert(profile);
    UserDto userDto = userService.update(userId, userUpdateRequest,
        Optional.ofNullable(profileRequest));
    log.info("사용자 정보 수정 완료 - ID: {}", userId);
    return ResponseEntity.status(HttpStatus.OK).body(userDto);

  }

  @DeleteMapping(path = "{userId}")
  @Override
  public ResponseEntity<Void> delete(@PathVariable("userId") UUID userId) {
    log.info("DELETE /api/users/{} - 사용자 삭제 요청", userId);
    userService.delete(userId);
    log.info("사용자 삭제 완료 - ID: {}", userId);
    return ResponseEntity
        .status(HttpStatus.NO_CONTENT)
        .build();
  }

  @GetMapping
  @Override
  public ResponseEntity<List<UserDto>> findAll() {
    log.info("GET /api/users - 사용자 전체 조회 요청");
    List<UserDto> users = userService.findAll();
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(users);
  }

  @PatchMapping(path = "{userId}/userStatus")
  @Override
  public ResponseEntity<UserStatusDto> updateUserStatusByUserId(@PathVariable("userId") UUID userId,
      @RequestBody @Valid UserStatusUpdateRequest request) {
    log.info("PATCH /api/users/{}/userStatus - 사용자 상태 변경 요청: {}", userId, request);
    UserStatusDto userStatusDto = userStatusService.updateByUserId(userId, request);
    log.info("사용자 상태 변경 완료 - ID: {}", userId);
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(userStatusDto);
  }

}
