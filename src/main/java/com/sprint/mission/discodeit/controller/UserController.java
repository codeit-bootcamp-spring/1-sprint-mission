package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.user.*;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import com.sprint.mission.discodeit.service.basic.AuthService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;
  private final UserStatusService userStatusService;

  @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
  public ResponseEntity<User> createUser(
      @RequestPart(value = "userCreateRequest") UserCreateRequest userCreateRequest,
      // multipart/form-data 형식으로 요청보낼 때 파일의 키 이름을 "binaryContent"
      @RequestPart(value = "binaryContent", required = false) MultipartFile file) throws Exception {

    BinaryContentCreateRequest binaryContentCreateRequest;
    if (file != null) {
      binaryContentCreateRequest = new BinaryContentCreateRequest(file);
    } else {
      binaryContentCreateRequest = null;
    }

    User user = userService.createUser(userCreateRequest, binaryContentCreateRequest);

//    UserCreateResponse userCreateResponse = userService.createUser(userCreateRequest,
//        binaryContentCreateRequest);  // 스프린트 미션 5 심화 조건 중 API 스펙을 준수
    return ResponseEntity.status(HttpStatus.CREATED).body(user); // 201
  }

  @PatchMapping(value = "/{userId}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
  public ResponseEntity<User> updateUser(@PathVariable UUID userId,
      @RequestPart(value = "userUpdateRequest") UserUpdateRequest userUpdateRequest,
      @RequestPart(value = "binaryContent", required = false) MultipartFile file) throws Exception {

    BinaryContentCreateRequest binaryContentCreateRequest;
    if (file != null) {
      binaryContentCreateRequest = new BinaryContentCreateRequest(file);
    } else {
      binaryContentCreateRequest = null;
    }

    return ResponseEntity.ok(userService.updateUserInfo(userId, userUpdateRequest,
        binaryContentCreateRequest)); // 스프린트 미션 5 심화 조건 중 API 스펙을 준수

  }

  @PatchMapping(value = "/{userId}/userStatus") // 스프린트 미션 5 심화 조건 중 API 스펙을 준수
  public ResponseEntity<UserStatus> updateUserStateByUserId(@PathVariable UUID userId,
      @RequestBody UserStatusUpdateByUserIdRequest userStatusUpdateByUserIdRequest) {
    return ResponseEntity.ok(
        userStatusService.updateUserStatusByUserId(userId, userStatusUpdateByUserIdRequest)); // 200
  }

  @DeleteMapping(value = "/{userId}")
  public ResponseEntity<Void> deleteUser(@PathVariable("userId") UUID id) {
    userService.removeUserById(id);
    // ResponseEntity.noContent().build() → 204 No Content
    return ResponseEntity.noContent().build(); // 204
  }

  @GetMapping
  public ResponseEntity<List<UserFindResponse>> findAllUsers() {
    return ResponseEntity.ok(userService.showAllUsers()); // 200
  }

}
