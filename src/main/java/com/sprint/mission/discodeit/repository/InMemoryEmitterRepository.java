package com.sprint.mission.discodeit.repository;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Slf4j
@Repository
public class InMemoryEmitterRepository {

  private final Map<UUID, ConcurrentHashMap<String, SseEmitter>> emitters = new ConcurrentHashMap<>();

  public void save(UUID userId, String emitterId, SseEmitter emitter) {
    emitters.computeIfAbsent(userId, k -> new ConcurrentHashMap<>())
        .put(emitterId, emitter);
  }

  public Collection<SseEmitter> findEmitters(UUID userId) {
    return emitters.getOrDefault(userId, new ConcurrentHashMap<>()).values();
  }

  public void delete(UUID userId, String emitterId) {
    Map<String,SseEmitter> map = emitters.get(userId);
    if (map != null) { map.remove(emitterId); }
  }
}
