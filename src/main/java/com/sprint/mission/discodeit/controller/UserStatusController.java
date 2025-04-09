package com.sprint.mission.discodeit.controller;


import com.sprint.mission.discodeit.controller.openapi.UserStatusApiDocs;
import com.sprint.mission.discodeit.dto.user_status.UpdateUserStatusDto;
import com.sprint.mission.discodeit.dto.user_status.UserStatusResponseDto;
import com.sprint.mission.discodeit.service.UserStatusService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserStatusController implements UserStatusApiDocs {

  private final UserStatusService userStatusService;

  @Override
  @PatchMapping("/users/{userId}/userStatus")
  public ResponseEntity<UserStatusResponseDto> updateUserStatus(@PathVariable String userId,
      @Valid @RequestBody UpdateUserStatusDto userStatusDto) {

    UserStatusResponseDto status = userStatusService.updateByUserId(userId, userStatusDto);
    return ResponseEntity.ok(status);
  }
}
