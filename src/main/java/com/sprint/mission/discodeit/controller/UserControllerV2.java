package com.sprint.mission.discodeit.controller;


import com.sprint.mission.discodeit.entity.status.user.UserStatUpdateRequest;
import com.sprint.mission.discodeit.entity.status.user.UserStatusUpdateResponse;
import com.sprint.mission.discodeit.entity.user.create.UserCreateRequest;
import com.sprint.mission.discodeit.entity.user.create.UserCreateResponse;
import com.sprint.mission.discodeit.entity.user.find.UserFindResponse;
import com.sprint.mission.discodeit.entity.user.update.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.user.update.UserUpdateResponse;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.status.UserStateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "User Controller")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserControllerV2 {

  private final UserService userService;
  private final UserStateService userStateService;

  /**
   * 유저 생성, 프사 선택
   */
  @Operation(summary = "유저 생성")
  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping
  public UserCreateResponse createUser(
      @RequestPart("userCreateRequest") @Validated UserCreateRequest userCreateRequest,
      @RequestPart(value = "profile", required = false) MultipartFile profile) throws IOException {

    return userService.createUserWithProfile(userCreateRequest, profile);
  }

  /**
   * 전체 user 목록 조회
   */
  @Operation(summary = "전체 유저 목록 조회")
  @GetMapping
  public List<UserFindResponse> findUsers() {
    return userService.findAllUsers();
  }

  /**
   * 유저 삭제
   */
  @Operation(summary = "유저 삭제")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @DeleteMapping("/{userId}")
  public UUID userDelete(@PathVariable("userId") UUID userId) {
    return userService.deleteUser(userId);
  }

  /**
   * 유저 정보 수정
   */
  @Operation(summary = "유저 정보 수정")
  @PatchMapping("/{userId}")
  public UserUpdateResponse updateUser(@PathVariable("userId") UUID userId,
      @RequestPart("userUpdateRequest") UserUpdateRequest userUpdateRequest,
      @RequestPart(value = "profile", required = false) MultipartFile profile) throws IOException {

    return userService.updateUser(userId, userUpdateRequest, profile);
  }

  /**
   * user 온라인 상태 업데이트
   */
  @Operation(summary = "유저 온라인 상태 업데이트")
  @PatchMapping("/{userId}/userStatus")
  public UserStatusUpdateResponse updateUserStatus(@PathVariable("userId") UUID userId,
      @RequestBody UserStatUpdateRequest request) {
    return userStateService.updateByUserId(userId, request);
  }
}
