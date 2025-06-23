package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.response.UserDto;
import com.sprint.mission.discodeit.security.jwt.JwtHeader;
import com.sprint.mission.discodeit.security.jwt.JwtUtils;
import com.sprint.mission.discodeit.service.notification.NotificationService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/api/sse")
@RequiredArgsConstructor
public class SSEController {

  private final NotificationService notificationService;
  private final JwtUtils jwtUtils;

  @GetMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE)
  public SseEmitter connect(
      @RequestHeader(JwtHeader.JWT_HEADER) String token,
      @RequestParam(value = "lastEventId", required = false) UUID lastEventId) {

    UserDto userDto = jwtUtils.parseUserDto(token);

    return notificationService.subscribe(userDto.id(), lastEventId);
  }
}
