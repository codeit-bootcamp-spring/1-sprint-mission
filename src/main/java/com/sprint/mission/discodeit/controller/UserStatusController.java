package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.userStatus.UserStatusDto;
import com.sprint.mission.discodeit.service.UserStatusService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/userStatus")
@RequiredArgsConstructor
@Tag(name = "User Status", description = "사용자 온라인 상태 API")
public class UserStatusController {

  private final UserStatusService userStatusService;

  @GetMapping
  public List<UserStatusDto> getAllUserStatus() {
    return userStatusService.findAll();
  }
}
