package com.sprint.mission.discodeit.service.basic;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Slf4j
@Service
public class BasicSseService {

  private final Map<UUID, Map<String, SseEmitter>> emitters = new ConcurrentHashMap<>();
  private static final long TIMEOUT = 60 * 1000L;

  public SseEmitter connect(UUID userId, String lastEventId) {
    SseEmitter emitter = new SseEmitter(TIMEOUT);
    String emitterId = UUID.randomUUID().toString();

    emitters.computeIfAbsent(userId, k -> new ConcurrentHashMap<>()).put(emitterId, emitter);
    log.info("SSE 연결 생성: userId={}, emitterId={}", userId, emitterId);

    emitter.onCompletion(() -> removeEmitter(userId, emitterId));
    emitter.onTimeout(() -> removeEmitter(userId, emitterId));
    emitter.onError((e) -> removeEmitter(userId, emitterId));

    try {
      emitter.send(SseEmitter.event()
          .name("connect")
          .id(emitterId)
          .data("SSE Connected"));
    } catch (IOException e) {
      emitter.completeWithError(e);
    }

    return emitter;
  }

  public void send(UUID userId, String eventName, Object data) {
    Map<String, SseEmitter> userEmitters = emitters.get(userId);
    if (userEmitters != null) {
      for (Map.Entry<String, SseEmitter> entry : userEmitters.entrySet()) {
        String emitterId = entry.getKey();
        SseEmitter emitter = entry.getValue();
        try {
          emitter.send(SseEmitter.event()
              .id(UUID.randomUUID().toString())
              .name(eventName)
              .data(data));
        } catch (IOException e) {
          log.warn("SSE 전송 실패: userId={}, emitterId={}", userId, emitterId);
          removeEmitter(userId, emitterId);
        }
      }
    }
  }

  private void removeEmitter(UUID userId, String emitterId) {
    Map<String, SseEmitter> userEmitters = emitters.get(userId);
    if (userEmitters != null) {
      userEmitters.remove(emitterId);
      if (userEmitters.isEmpty()) {
        emitters.remove(userId);
      }
    }
    log.info("SSE 연결 제거: userId={}, emitterId={}", userId, emitterId);
  }

  @Scheduled(fixedRate = 30000)
  public void sendPing() {
    emitters.forEach((userId, emitterMap) -> {
      emitterMap.forEach((emitterId, emitter) -> {
        try {
          emitter.send(SseEmitter.event()
              .name("ping")
              .data("keep-alive"));
        } catch (IOException e) {
          removeEmitter(userId, emitterId);
        }
      });
    });
  }
}
