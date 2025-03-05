package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.swagger.UserApi;
import com.sprint.mission.discodeit.dto.user.UserCreateDTO;
import com.sprint.mission.discodeit.dto.user.UserFindDTO;
import com.sprint.mission.discodeit.dto.user.UserUpdateDTO;
import com.sprint.mission.discodeit.dto.userStatus.UserStatusUpdateDTO;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController implements UserApi {

  private final UserService userService;
  private final UserStatusService userStatusService;

  @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
  public ResponseEntity<User> create(
      @RequestPart("userCreateRequest") UserCreateDTO userCreateRequest,
      @RequestPart(value = "profile", required = false) MultipartFile profile
  ) {
    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(userService.create(userCreateRequest, profile));
  }

  @PatchMapping("{userId}")
  public ResponseEntity<User> update(@PathVariable UUID userId,
      @RequestPart("userUpdateDTO") UserUpdateDTO userUpdateRequest,
      @RequestPart(value = "profile", required = false) MultipartFile profile
  ) {
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(userService.update(userId, userUpdateRequest, profile));
  }

  @DeleteMapping("{userId}")
  public ResponseEntity<Void> delete(@PathVariable UUID userId) {
    userService.delete(userId);
    return ResponseEntity
        .status(HttpStatus.NO_CONTENT)
        .build();
  }

  @GetMapping
  public ResponseEntity<List<UserFindDTO>> findAll() {
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(userService.findAll());
  }

  @PatchMapping("{userId}/userStatus")
  public ResponseEntity<UserStatus> updateUserStatusByUserId(@PathVariable UUID userId,
      @RequestBody UserStatusUpdateDTO request) {
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(userStatusService.updateByUserId(userId, request.getNewLastActiveAt()));
  }

}
