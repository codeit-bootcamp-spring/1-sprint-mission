package com.sprint.mission.discodeit.repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Repository
@RequiredArgsConstructor
public class SseEmitterRepository {

  private final Map<String, Map<String, SseEmitter>> emitters = new ConcurrentHashMap<>();


  public void save(String username, String emitterId, SseEmitter emitter) {
    emitters.computeIfAbsent(username, k -> new ConcurrentHashMap<>())
        .put(emitterId, emitter);
  }

  public void remove(String username, String emitterId) {
    Map<String, SseEmitter> userEmitters = emitters.get(username);
    if (userEmitters != null) {
      userEmitters.remove(emitterId);
    }
  }

  public List<SseEmitter> findAllEmittersByUsername(String username) {
    Map<String, SseEmitter> userEmitters = emitters.get(username);
    return userEmitters == null ? Collections.emptyList() : new ArrayList<>(userEmitters.values());
  }

  public Map<String, List<SseEmitter>> findAllEmitters() {
    return emitters.entrySet().stream()
        .collect(Collectors.toMap(
            Map.Entry::getKey,
            e -> new ArrayList<>(e.getValue().values())
        ));
  }

}
