package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.repository.SseEmitterRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/sse")
public class SseController {

  private final SseEmitterRepository emitterRepository;

  @GetMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE)
  public SseEmitter connect(
      @RequestHeader("X-USER-ID") UUID userId,
      @RequestHeader(value = "Last-Event-ID", required = false) String lastEventId
  ) {
    SseEmitter emitter = new SseEmitter(60 * 1000L * 10); // 10분
    emitterRepository.add(userId, emitter);
    log.info("SSE 연결됨: userId={}, lastEventId={}", userId, lastEventId);
    return emitter;
  }
}

