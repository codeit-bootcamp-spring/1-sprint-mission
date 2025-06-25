package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.security.CustomUserDetails;
import com.sprint.mission.discodeit.service.SseService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/api/sse")
@RequiredArgsConstructor
public class SseController {

  private final SseService sseService;

  @GetMapping
  public SseEmitter connectSse(@AuthenticationPrincipal CustomUserDetails userDetails,
      @RequestHeader(value = "Last-Event-ID", required = false) String lastEventId) {

    return sseService.subscribe(userDetails.getUser().getId(), lastEventId);
  }

}
