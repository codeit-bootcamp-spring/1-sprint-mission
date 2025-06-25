package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.repository.EmitterRepository;
import com.sprint.mission.discodeit.service.SseService;
import java.util.Map;
import java.util.function.Consumer;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class SseController {

  private final EmitterRepository emitters;
  private final SseService sseService;

  @GetMapping(value = "/sse", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
  public SseEmitter connect(
      @AuthenticationPrincipal Jwt jwt,
      @RequestHeader(value = "Last-Event-ID", required = false) String lastId) {

    if (jwt == null) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
    }
    Long userId = ((Number)((Map<?,?>)jwt.getClaim("userDto")).get("id")).longValue();

    SseEmitter emitter = emitters.create(userId);

    // 연결이 끊긴 경우 클리너로 정리
    Consumer<SseEmitter> cleaner = e -> emitters.remove(userId, e);
    emitter.onCompletion(() -> cleaner.accept(emitter));
    emitter.onTimeout   (() -> cleaner.accept(emitter));
    emitter.onError     (ex -> cleaner.accept(emitter));

    sseService.sendInitEvent(emitter, lastId);
    return emitter;
  }
}
