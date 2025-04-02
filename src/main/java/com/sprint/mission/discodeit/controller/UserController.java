package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.dto.UserStatusDto;
import com.sprint.mission.discodeit.dto.user.*;
import com.sprint.mission.discodeit.dto.userStatus.UserStatusUpdateByUserIdRequest;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import jakarta.validation.Valid;
import java.util.List;
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
public class UserController {

  private final UserService userService;
  private final UserStatusService userStatusService;

  @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
  public ResponseEntity<UserDto> createUser(
      @Valid @RequestPart(value = "userCreateRequest") UserCreateRequest userCreateRequest,
      @RequestPart(value = "binaryContent", required = false) MultipartFile file) throws Exception {

    /* 유저 생성 요청(Request) */
    log.info("유저 생성 요청(Request): username={}, hasProfileImage={}",
        userCreateRequest.username(),
        file != null);

    // 프로필 이미지 처리 
    /* TODO(멘토님께) : 삼항 연산자를 쓰면 가독성이 떨어진다는 이야기를 들었는데,
     *  아래(주석 처리)와 같이 구현하는 게 좋을까요, 아니면 이런 식(삼항 연산자 이용)으로 구현하는 것도 추천될 수 있을까요?
     */
    BinaryContentCreateRequest binaryContentCreateRequest =
        (file != null) ? new BinaryContentCreateRequest(file) : null;

    if (binaryContentCreateRequest != null) {
      log.debug("프로필 이미지 생성 : filename={}, size={}, contentType={}",
          binaryContentCreateRequest.fileName(),
          binaryContentCreateRequest.size(),
          binaryContentCreateRequest.contentType());
    }

    /**
     BinaryContentCreateRequest binaryContentCreateRequest;
     if (file != null) {
     binaryContentCreateRequest = new BinaryContentCreateRequest(file);
     log.debug("프로필 이미지 생성 요청(Request): filename={}, size={}, contentType={}",
     binaryContentCreateRequest.fileName(), binaryContentCreateRequest.size(),
     binaryContentCreateRequest.contentType());
     } else {
     binaryContentCreateRequest = null;
     }
     **/

    // 유저 생성
    UserDto userDto = userService.createUser(userCreateRequest, binaryContentCreateRequest);
    /* 유저 생성 응답(Response) */
    log.info("유저 생성 응답(Response): username={}, HttpStatus={} ",
        userDto.username(),
        HttpStatus.CREATED);
    return ResponseEntity.status(HttpStatus.CREATED).body(userDto);
  }

  @PatchMapping(value = "/{userId}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
  public ResponseEntity<UserDto> updateUser(@PathVariable UUID userId,
      @Valid @RequestPart(value = "userUpdateRequest") UserUpdateRequest userUpdateRequest,
      @RequestPart(value = "profile", required = false) MultipartFile file) throws Exception {
    log.info(
        "유저 수정 요청(Request): usernameChanged={}, emailChanged={}, passwordChanged={}, hasProfileImage={}",
        userUpdateRequest.newUsername() != null,
        userUpdateRequest.newEmail() != null,
        userUpdateRequest.newPassword() != null,
        file != null && !file.isEmpty()
    );

    // 새로운 프로필 이미지 처리
    BinaryContentCreateRequest binaryContentCreateRequest =
        (file != null) ? new BinaryContentCreateRequest(file) : null;
    if (binaryContentCreateRequest != null) {
      log.debug("프로필 이미지 생성 : filename={}, size={}, contentType={}",
          binaryContentCreateRequest.fileName(),
          binaryContentCreateRequest.size(),
          binaryContentCreateRequest.contentType());
    }

    // 유저 수정
    UserDto userDto = userService.updateUserInfo(userId, userUpdateRequest,
        binaryContentCreateRequest);
    log.info("유저 수정 응답(Response): username={}, HttpStatus={} ",
        userDto.username(),
        HttpStatus.OK);
    return ResponseEntity.ok(userDto);

  }

  @PatchMapping(value = "/{userId}/userStatus")
  public ResponseEntity<UserStatusDto> updateUserStateByUserId(@PathVariable UUID userId,
      @RequestBody UserStatusUpdateByUserIdRequest userStatusUpdateByUserIdRequest) {

    // 유저 상태 수정
    UserStatusDto userStatusDto = userStatusService.updateUserStatusByUserId(userId,
        userStatusUpdateByUserIdRequest);

    return ResponseEntity.ok(userStatusDto);
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

}
