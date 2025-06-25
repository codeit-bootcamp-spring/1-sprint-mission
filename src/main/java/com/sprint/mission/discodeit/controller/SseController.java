package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.security.DiscodeitUserDetails;
import com.sprint.mission.discodeit.service.SseService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequiredArgsConstructor
public class SseController {

  private final SseService sseService;

  @GetMapping("/api/sse")
  public SseEmitter connect(
      @RequestHeader(value = "Last-Event-ID", required = false) String lastEventId,
      @AuthenticationPrincipal DiscodeitUserDetails principal) {

    UUID userId = principal.getId();
    return sseService.subscribe(userId, lastEventId);
  }
}
