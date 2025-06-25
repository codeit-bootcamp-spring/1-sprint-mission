package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.repository.EmitterRepository;
import com.sprint.mission.discodeit.service.basic.BasicSseService;
import jakarta.servlet.http.HttpServletRequest;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/sse")
public class SseController {

  private final BasicSseService sseService;
  private final EmitterRepository emitterRepository;

  public SseEmitter connect(HttpServletRequest request,
      @RequestParam UUID userId,
      @RequestHeader(value = "Last-Event-ID", required = false) String lastEventId) {
    SseEmitter emitter = new SseEmitter(60 * 1000L * 30);

    emitterRepository.add(userId, emitter);

    emitter.onCompletion(() -> emitterRepository.remove(userId, emitter));
    emitter.onTimeout(() -> emitterRepository.remove(userId, emitter));
    emitter.onError((ex) -> emitterRepository.remove(userId, emitter));

    return emitter;
  }

}
