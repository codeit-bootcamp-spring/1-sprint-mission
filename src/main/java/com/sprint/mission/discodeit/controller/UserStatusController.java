package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.userStatus.UpdateUserStatusDto;
import com.sprint.mission.discodeit.dto.userStatus.UserStatusResponseDto;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user-status")
@RequiredArgsConstructor
@Tag(name = "User Status", description = "사용자 온라인 상태 API")
public class UserStatusController {

  private final UserStatusService userStatusService;

  @PutMapping("/{userId}")
  public UserStatusResponseDto getUserStatus(@PathVariable String userId,
      @RequestBody UpdateUserStatusDto updateUserStatusDto) {
    return userStatusService.updateByUserId(userId, updateUserStatusDto);
  }

  @GetMapping
  public List<UserStatusResponseDto> getAllUserStatus() {
    return userStatusService.findAll();
  }
}
