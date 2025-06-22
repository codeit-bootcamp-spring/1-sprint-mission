package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.security.DiscodeitUserDetails;
import com.sprint.mission.discodeit.service.SseService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class SseController {

  private final SseService sseService;

  // SSE 연결 엔드포인트
  // 클라이언트가 GET 요청으로 /api/sse 에 접속하면 SSE 연결이 시작됨
  @GetMapping(value = "/sse", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
  public ResponseEntity<SseEmitter> streamData(
      @RequestHeader(value = "Last-Event-ID", required = false) String lastEventId) {

    // SecurityContext에서 현재 사용자 ID 가져오기
    UUID currentUserId = getCurrentUserId();

    // SseService의 subscribe 메서드로 구독
    return ResponseEntity.ok(sseService.subscribe(currentUserId, lastEventId));
  }

  private UUID getCurrentUserId() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    if (auth == null || !auth.isAuthenticated()) {
      throw new SecurityException("인증되지 않은 사용자");
    }
    DiscodeitUserDetails userDetails = (DiscodeitUserDetails) auth.getPrincipal();
    return userDetails.getUserDto().id();
  }

}
