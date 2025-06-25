package com.sprint.mission.discodeit.repository;

import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Repository
public class EmitterRepository {

  // Thread-Safe 한 메모리 구조로 관리
  private final Map<UUID, Set<SseEmitter>> emitters = new ConcurrentHashMap<>();

  public void save(UUID userId, SseEmitter emitter) {
    emitters.computeIfAbsent(userId, k -> new CopyOnWriteArraySet<>()).add(emitter);
  }

  public void remove(UUID userId, SseEmitter emitter) {
    Set<SseEmitter> userEmitters = emitters.get(userId);
    if (userEmitters != null) {
      userEmitters.remove(emitter);
      if (userEmitters.isEmpty()) {
        emitters.remove(userId);
      }
    }
  }

  public Set<SseEmitter> get(UUID userId) {
    return emitters.getOrDefault(userId, Set.of());
  }

  public Set<UUID> getAllUserIds() {
    return emitters.keySet();
  }
}
