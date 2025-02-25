package com.sprint.mission.discodeit.controller;


import com.sprint.mission.discodeit.dto.ApiResponse;
import com.sprint.mission.discodeit.dto.user.UserCreateDTO;
import com.sprint.mission.discodeit.dto.user.UserFindDTO;
import com.sprint.mission.discodeit.dto.user.UserUpdateDTO;
import com.sprint.mission.discodeit.dto.userStatus.UserStatusUpdateDTO;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

  private final UserService userService;
  private final UserStatusService userStatusService;

  @PostMapping
  public ApiResponse<UUID> create(@ModelAttribute UserCreateDTO requset) {
    return ApiResponse.<UUID>builder()
        .code(HttpStatus.CREATED.value())
        .message("사용자 등록 완료")
        .data(userService.create(requset))
        .build();
  }

  @GetMapping
  public ApiResponse<List<UserFindDTO>> findAll() {
    return ApiResponse.<List<UserFindDTO>>builder()
        .code(HttpStatus.OK.value())
        .message("모든 사용자 조회")
        .data(userService.findAll())
        .build();
  }

  @PutMapping("{userId}")
  public ApiResponse update(@PathVariable UUID userId, @ModelAttribute UserUpdateDTO request) {
    userService.update(userId, request);
    return ApiResponse.builder()
        .code(HttpStatus.OK.value())
        .message("사용자 수정 완료")
        .build();
  }

  @DeleteMapping("{userId}")
  public ApiResponse<UUID> delete(@PathVariable UUID userId) {
    return ApiResponse.<UUID>builder()
        .code(HttpStatus.NO_CONTENT.value())
        .message("사용자 삭제 완료")
        .data(userService.delete(userId))
        .build();
  }

  @PutMapping("{userId}/status")
  public ApiResponse<UserStatus> updateUserStatusByUserId(@PathVariable UUID userId,
      @RequestBody UserStatusUpdateDTO request) {
    UserStatus userStatus = userStatusService.update(userId, request);
    return ApiResponse.<UserStatus>builder()
        .code(HttpStatus.OK.value())
        .message("사용자 온라인 상태 업데이트 완료")
        .data(userStatus)
        .build();
  }

}
