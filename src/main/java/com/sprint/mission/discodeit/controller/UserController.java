
package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.api.UserApi;
import com.sprint.mission.discodeit.dto.binarycontent.CreateBinaryContentRequest;
import com.sprint.mission.discodeit.dto.user.CreateUserRequest;
import com.sprint.mission.discodeit.dto.user.UpdateUserRequest;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.dto.userStatusDto.UpdateUserStatusRequest;
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
      @Valid @RequestPart("userRequest") CreateUserRequest userRequest,
      @RequestPart(value = "profile", required = false) MultipartFile profile) {

    log.info("User 생성 요청 : {}", userRequest);

    Optional<CreateBinaryContentRequest> profileRequest = BinaryContentUtil.convertToBinaryContentRequest(
        profile);
    UserDto userDto = userService.create(userRequest, profileRequest);

    log.debug("User 생성 응답 : {}", userDto);

    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(userDto);
  }

  @Override
  @PatchMapping(path = "{userId}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
  public ResponseEntity<UserDto> update(
      @PathVariable("userId") UUID userId,
      @Valid @RequestPart("userRequest") UpdateUserRequest userRequest,
      @RequestPart("profile") MultipartFile profile) {

    log.info("User 수정 요청 : userId={}, request={}", userId, userRequest);

    Optional<CreateBinaryContentRequest> profileRequest = BinaryContentUtil.convertToBinaryContentRequest(
        profile);
    UserDto userDto = userService.update(userId, userRequest, profileRequest);

    log.debug("User 수정 응답 : {}", userDto);

    return ResponseEntity
        .status(HttpStatus.OK)
        .body(userDto);
  }

  @Override
  @DeleteMapping(path = "{userId}")
  public ResponseEntity<Void> delete(@PathVariable("userId") UUID userId) {

    log.info("User 삭제 요청 : userId={}", userId);

    userService.delete(userId);

    log.debug("User 삭제 성공 : userId={}", userId);

    return ResponseEntity
        .status(HttpStatus.NO_CONTENT)
        .build();
  }

  @Override
  @GetMapping
  public ResponseEntity<List<UserDto>> findAll() {

    List<UserDto> userDtos = userService.findAll();

    return ResponseEntity
        .status(HttpStatus.OK)
        .body(userDtos);
  }

  @Override
  @PatchMapping(path = "{userId}/userStatus")
  public ResponseEntity<UserStatusDto> updateUserStatusByUserId(
      @PathVariable("userId") UUID userId,
      @Valid @RequestBody UpdateUserStatusRequest request) {

    log.info("UserStatus 수정 요청 : userId={}, request={}", userId, request);

    UserStatusDto userStatusDto = userStatusService.updateByUserId(userId, request);

    log.debug("UserStatus 수정 응답 : {}", userStatusDto);

    return ResponseEntity
        .status(HttpStatus.OK)
        .body(userStatusDto);
  }
}