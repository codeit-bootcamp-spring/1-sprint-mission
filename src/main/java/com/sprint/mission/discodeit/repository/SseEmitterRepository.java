package com.sprint.mission.discodeit.repository;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Component
@Slf4j
public class SseEmitterRepository {

  private final Map<UUID, List<SseEmitter>> emitters = new ConcurrentHashMap<>();

  public void add(UUID userId, SseEmitter emitter) {
    emitters.computeIfAbsent(userId, k -> new CopyOnWriteArrayList<>()).add(emitter);
    emitter.onCompletion(() -> remove(userId, emitter));
    emitter.onTimeout(() -> remove(userId, emitter));
    emitter.onError(e -> remove(userId, emitter));
  }

  public List<SseEmitter> get(UUID userId) {
    return emitters.getOrDefault(userId, List.of());
  }

  public void remove(UUID userId, SseEmitter emitter) {
    List<SseEmitter> list = emitters.get(userId);
    if (list != null) {
      list.remove(emitter);
      if (list.isEmpty()) emitters.remove(userId);
    }
  }

  public void send(UUID userId, String eventName, Object data) {
    List<SseEmitter> list = get(userId);
    String id = UUID.randomUUID().toString();

    for (SseEmitter emitter : list) {
      try {
        emitter.send(SseEmitter.event()
            .id(id)
            .name(eventName)
            .data(data, MediaType.APPLICATION_JSON));
      } catch (IOException e) {
        log.warn("SSE 전송 실패: {}", e.getMessage());
        remove(userId, emitter);
      }
    }
  }

  public Set<UUID> getAllUserIds() {
    return emitters.keySet();
  }
}

