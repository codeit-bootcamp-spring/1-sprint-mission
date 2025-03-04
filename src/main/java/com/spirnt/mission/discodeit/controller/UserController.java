package com.spirnt.mission.discodeit.controller;

import com.spirnt.mission.discodeit.dto.binaryContent.BinaryContentCreateRequest;
import com.spirnt.mission.discodeit.dto.user.UserCreateRequest;
import com.spirnt.mission.discodeit.dto.user.UserDto;
import com.spirnt.mission.discodeit.dto.user.UserUpdateRequest;
import com.spirnt.mission.discodeit.dto.userStatus.UserStatusUpdateRequest;
import com.spirnt.mission.discodeit.enity.User;
import com.spirnt.mission.discodeit.enity.UserStatus;
import com.spirnt.mission.discodeit.service.UserService;
import com.spirnt.mission.discodeit.service.UserStatusService;
import com.spirnt.mission.discodeit.swagger.UserApiDocs;
import java.util.List;
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
public class UserController implements UserApiDocs {


  private final UserService userService;
  private final UserStatusService userStatusService;

  // 모든 사용자 조회
  @GetMapping(value = "")
  public ResponseEntity<List<UserDto>> getAllUsers() {
    return ResponseEntity.ok(userService.findAll());
  }


  // 사용자 등록
  @PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<User> createUser(@RequestPart UserCreateRequest userCreateRequest,
      @RequestPart(required = false) MultipartFile profile) {
    BinaryContentCreateRequest binaryContentCreateRequest =
        (profile == null) ? null : new BinaryContentCreateRequest(profile);
    User user = userService.create(userCreateRequest, binaryContentCreateRequest);
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(user);
  }

  // 사용자 정보 수정
  @PatchMapping(value = "/{userId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<User> updateUser(@PathVariable UUID userId,
      @RequestPart UserUpdateRequest userUpdateRequest,
      @RequestPart(required = false) MultipartFile profile) {
    BinaryContentCreateRequest binaryContentCreateRequest =
        (profile == null) ? null : new BinaryContentCreateRequest(profile);
    User user = userService.update(userId, userUpdateRequest, binaryContentCreateRequest);
    return ResponseEntity.ok(user);
  }

  // 사용자 삭제
  @DeleteMapping("/{userId}")
  public ResponseEntity<Void> deleteUser(@PathVariable UUID userId) {
    userService.delete(userId);
    return ResponseEntity.noContent().build();
  }

  // 사용자의 온라인 상태 업데이트
  @PatchMapping(value = "/{userId}/userStatus", consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<UserStatus> updateUserStatus(@PathVariable UUID userId,
      @RequestBody UserStatusUpdateRequest userStatusUpdateRequest) {
    UserStatus userStatus = userStatusService.updateByUserId(userId, userStatusUpdateRequest);
    return ResponseEntity.ok(userStatus);
  }

}

