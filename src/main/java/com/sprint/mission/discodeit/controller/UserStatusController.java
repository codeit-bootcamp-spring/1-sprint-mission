package com.sprint.mission.discodeit.controller;


import com.sprint.mission.discodeit.dto.user_status.UpdateUserStatusDto;
import com.sprint.mission.discodeit.dto.user_status.UserStatusResponseDto;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserStatusController {

  private final UserStatusService userStatusService;

  @PatchMapping("/users/{userId}/userStatus")
  public ResponseEntity<UserStatusResponseDto> updateUserStatus(@PathVariable String userId, @RequestBody UpdateUserStatusDto userStatusDto){

    UserStatusResponseDto status = userStatusService.updateByUserId(userId, userStatusDto);

    return ResponseEntity.ok(status);
  }
}
