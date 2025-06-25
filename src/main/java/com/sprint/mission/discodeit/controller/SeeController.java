package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.service.SseService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Slf4j
@RestController
@RequestMapping("/api/sse")
@RequiredArgsConstructor
public class SseController {

  private final SseService sseService;

  @GetMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE)
  public SseEmitter connect(
      @AuthenticationPrincipal UserDetails userDetails,
      @RequestHeader(value = "Last-Event-ID", required = false) String lastEventId) {

    // UserDetails에서 userId 추출 (실제 구현에 맞게 조정 필요)
    UUID userId = UUID.fromString(userDetails.getUsername());

    log.info("SSE 연결 요청: userId={}, lastEventId={}", userId, lastEventId);

    return sseService.createConnection(userId, lastEventId);
  }
}