package com.sprint.mission.discodeit.controller;


import com.sprint.mission.discodeit.entity.user.dto.UserCreateRequest;
import com.sprint.mission.discodeit.entity.user.dto.UserCreateResponse;
import com.sprint.mission.discodeit.entity.user.dto.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.user.dto.UserStatusUpdateResponse;
import com.sprint.mission.discodeit.entity.user.dto.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.user.dto.UserUpdateResponse;
import com.sprint.mission.discodeit.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
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
public class UserController {

  private final UserService userService;

  /**
   * 유저 생성, 프사 선택
   */
  @Operation(summary = "유저 생성")
  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping
  public UserCreateResponse createUser(
      @RequestPart("userCreateRequest") @Validated UserCreateRequest userCreateRequest,
      @RequestPart(value = "profile", required = false) MultipartFile profile) throws IOException {

    return userService.join(userCreateRequest, profile);
  }

  /**
   * 전체 user 목록 조회
   */
  @Operation(summary = "전체 유저 목록 조회")
  @GetMapping
  public List<UserCreateResponse> findUsers() {
    return userService.findAll();
  }

  /**
   * 유저 삭제
   */
  @Operation(summary = "유저 삭제")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @DeleteMapping("/{userId}")
  public UUID userDelete(@NotNull @PathVariable("userId") UUID userId) {
    return userService.delete(userId);
  }

  /**
   * 유저 정보 수정
   */
  @Operation(summary = "유저 정보 수정")
  @PatchMapping("/{userId}")
  public UserUpdateResponse updateUser(@PathVariable("userId") UUID userId,
      @RequestPart("userUpdateRequest") UserUpdateRequest userUpdateRequest,
      @RequestPart(value = "profile", required = false) MultipartFile profile) throws IOException {

    return userService.update(userId, userUpdateRequest, profile);
  }

  /**
   * user 온라인 상태 업데이트
   */
  @Operation(summary = "유저 온라인 상태 업데이트")
  @PatchMapping("/{userId}/userStatus")
  public UserStatusUpdateResponse updateUserStatus(@NotNull @PathVariable("userId") UUID userId,
      @Validated @RequestBody UserStatusUpdateRequest request) {
    return userService.updateOnlineStatus(userId, request);
  }
}
