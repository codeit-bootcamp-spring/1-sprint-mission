package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.security.DiscodeitUserDetails;
import com.sprint.mission.discodeit.service.basic.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequiredArgsConstructor
@Slf4j
public class SseController {

  private final NotificationService notificationService;

  /**
   * SSE 연결 엔드포인트
   *
   * @param lastEventId 이벤트 유실 복원을 위한 메소드 파라미터
   **/
  @GetMapping(value = "/api/sse", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
  public SseEmitter subscribe(
      @AuthenticationPrincipal DiscodeitUserDetails userDetails,
      @RequestHeader(value = "Last-Event-ID", required = false, defaultValue = "") String lastEventId
  ) {
    log.info("SSE 연결 시작");
    return notificationService.subscribe(userDetails.getUserDto().id().toString(), lastEventId);
  }
}
