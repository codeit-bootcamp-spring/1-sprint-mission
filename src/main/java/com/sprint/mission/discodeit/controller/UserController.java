
package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.api.UserApi;
import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserUpdateRequest;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.dto.userStatusDto.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.dto.userStatusDto.UserStatusDto;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import com.sprint.mission.discodeit.util.BinaryContentUtil;
import jakarta.validation.Valid;
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

@Slf4j
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController implements UserApi {

  private final UserService userService;
  private final UserStatusService userStatusService;

  @Override
  @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
  public ResponseEntity<UserDto> create(
      @Valid @RequestPart("userRequest") UserCreateRequest request,
      @RequestPart(value = "profile", required = false) MultipartFile profile) {

    log.info("사용자 생성 요청 : {}", request);
    Optional<BinaryContentCreateRequest> profileRequest = BinaryContentUtil.convertToBinaryContentRequest(
        profile);
    UserDto userDto = userService.create(request, profileRequest);
    log.debug("사용자 생성 응답 : {}", userDto);

    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(userDto);
  }

  @Override
  @PatchMapping(path = "{userId}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
  public ResponseEntity<UserDto> update(
      @PathVariable("userId") UUID userId,
      @Valid @RequestPart("userRequest") UserUpdateRequest request,
      @RequestPart("profile") MultipartFile profile) {

    log.info("사용자 수정 요청 : id={}, request={}", userId, request);

    Optional<BinaryContentCreateRequest> profileRequest = BinaryContentUtil.convertToBinaryContentRequest(
        profile);
    UserDto userDto = userService.update(userId, request, profileRequest);

    log.debug("사용자 수정 응답 : {}", userDto);

    return ResponseEntity
        .status(HttpStatus.OK)
        .body(userDto);
  }

  @Override
  @DeleteMapping(path = "{userId}")
  public ResponseEntity<Void> delete(@PathVariable("userId") UUID userId) {

    log.info("User 삭제 요청 : id={}", userId);

    userService.delete(userId);

    log.info("User 삭제 완료");

    return ResponseEntity
        .status(HttpStatus.NO_CONTENT)
        .build();
  }

  @Override
  @GetMapping
  public ResponseEntity<List<UserDto>> findAll() {

    log.info("사용자 전체 조회 요청");
    List<UserDto> userDtos = userService.findAll();
    log.debug("사용자 전체 조회 응답: count={}", userDtos.size());

    return ResponseEntity
        .status(HttpStatus.OK)
        .body(userDtos);
  }

  @Override
  @PatchMapping(path = "{userId}/userStatus")
  public ResponseEntity<UserStatusDto> updateUserStatusByUserId(
      @PathVariable("userId") UUID userId,
      @Valid @RequestBody UserStatusUpdateRequest request) {

    log.info("사용자 상태 수정 요청 : userId={}, request={}", userId, request);
    UserStatusDto userStatusDto = userStatusService.updateByUserId(userId, request);
    log.info("사용자 상태 수정 응답 : {}", userStatusDto);

    return ResponseEntity
        .status(HttpStatus.OK)
        .body(userStatusDto);
  }
}