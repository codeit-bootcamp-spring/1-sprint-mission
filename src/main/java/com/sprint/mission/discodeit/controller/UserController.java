package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.api.UserApi;
import com.sprint.mission.discodeit.dto.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.dto.user.*;
import com.sprint.mission.discodeit.service.UserService;
import io.micrometer.core.annotation.Timed;
import jakarta.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Slf4j // 로깅을 위한 Lombok 어노테이션 추가
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController implements UserApi {

  private final UserService userService;

  @Timed(
      value = "api.user.signup",
      description = "사용자 회원가입 API 응답 시간 -> 프로필 이미지 업로드 시간 체킹"
  )
  @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
  public ResponseEntity<UserDto> createUser(
      @Valid @RequestPart(value = "userCreateRequest") UserCreateRequest userCreateRequest,
      @RequestPart(value = "binaryContent", required = false) MultipartFile file) {
    /* 유저 생성 요청(Request) */
    log.info("유저 생성 요청(Request): username={}, hasProfileImage={}",
        userCreateRequest.username(),
        file != null);
    // 프로필 이미지 처리
    Optional<BinaryContentCreateRequest> profileRequest =
        Optional.ofNullable(file).flatMap(this::resolveProfileRequest);

    if (profileRequest.isPresent()) {
      log.debug("프로필 이미지 생성 : {}", profileRequest);
    }
    // 유저 생성
    UserDto userDto = userService.createUser(userCreateRequest, profileRequest);
    /* 유저 생성 응답(Response) */
    log.debug("유저 생성 응답(Response) {}", userDto);
    return ResponseEntity.status(HttpStatus.CREATED).body(userDto);
  }

  @PatchMapping(value = "/{userId}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
  public ResponseEntity<UserDto> updateUser(
      @PathVariable UUID userId,
      @Valid @RequestPart(value = "userUpdateRequest") UserUpdateRequest userUpdateRequest,
      @RequestPart(value = "profile", required = false) MultipartFile file
  ) {
    log.info(
        "유저 수정 요청(Request): userId={}, userUpdateRequest={}", userId, userUpdateRequest
    );
    // 새로운 프로필 이미지 처리
    Optional<BinaryContentCreateRequest> profileRequest =
        Optional.ofNullable(file).flatMap(this::resolveProfileRequest);
    if (profileRequest.isPresent()) {
      log.debug("프로필 이미지 생성 : {}", profileRequest);
    }
    // 유저 수정
    UserDto userDto = userService.updateUserInfo(userId, userUpdateRequest, profileRequest);
    log.debug("유저 수정 응답(Response) {}", userDto);
    return ResponseEntity.status(HttpStatus.OK).body(userDto);

  }

  @DeleteMapping(value = "/{userId}")
  public ResponseEntity<Void> deleteUser(@PathVariable("userId") UUID id) {
    log.info("유저 삭제 요청(Request)");

    // 유저 삭제
    userService.removeUserById(id);
    log.info("유저 삭제 응답(Response): HttpStatus={}", HttpStatus.NO_CONTENT);
    return ResponseEntity.noContent().build(); // 204
  }

  @GetMapping
  public ResponseEntity<List<UserDto>> findAllUsers() {
    // 유저 목록 조회
    return ResponseEntity.ok(userService.showAllUsers());
  }

  private Optional<BinaryContentCreateRequest> resolveProfileRequest(MultipartFile profile) {
    if (profile.isEmpty()) {
      return Optional.empty();
    } else {
      try {
        BinaryContentCreateRequest binaryContentCreateRequest =
            new BinaryContentCreateRequest(
                profile.getOriginalFilename(),
                profile.getSize(),
                profile.getContentType(),
                profile.getBytes()
            );
        return Optional.of(binaryContentCreateRequest);
      } catch (IOException e) {
        throw new RuntimeException();
      }
    }
  }
}
