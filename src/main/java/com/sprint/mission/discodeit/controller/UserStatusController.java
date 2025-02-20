package com.sprint.mission.discodeit.controller;


import com.sprint.mission.discodeit.dto.user_status.UpdateUserStatusDto;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class UserStatusController {

  private final UserStatusService userStatusService;

  @PatchMapping("/users/{userId}/statuses")
  public ResponseEntity<String> updateUserStatus(@PathVariable String userId, @RequestBody UpdateUserStatusDto userStatusDto){

    UserStatus status = userStatusService.updateByUserId(userId, userStatusDto);

    return ResponseEntity.ok("update successful : " + status.getUserStatus());
  }
}
