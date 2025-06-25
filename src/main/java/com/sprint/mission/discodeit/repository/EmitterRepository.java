package com.sprint.mission.discodeit.repository;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;
import lombok.Getter;
import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Getter
@Repository
public class EmitterRepository {
  private final ConcurrentMap<Long, CopyOnWriteArrayList<SseEmitter>> store = new ConcurrentHashMap<>();
  private static final long DEFAULT_TIMEOUT = 60 * 60 * 1000L; // 1시간


  public SseEmitter create(Long userId) {
    SseEmitter emitter = new SseEmitter(DEFAULT_TIMEOUT);
    store.computeIfAbsent(userId, k -> new CopyOnWriteArrayList<>()).add(emitter);
    return emitter;
  }

 public List<SseEmitter> get(Long userId) {
    return store.getOrDefault(userId, new CopyOnWriteArrayList<>());
  }

  public void remove(Long userId, SseEmitter emitter) {
    List<SseEmitter> list = store.get(userId);
    if (list != null) {
      list.remove(emitter);
    }
  }
}
