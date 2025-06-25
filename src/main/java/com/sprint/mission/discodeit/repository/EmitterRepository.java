package com.sprint.mission.discodeit.repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Repository
@Slf4j
public class EmitterRepository {

  /**
   * SseEmitter 객체를 Thread-safe한 메모리 구조에서 안전하게 관리
   * <p>
   * Thread-safe한 메모리 구조 = ConcurrentHashMap
   **/
  private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();
  private final Map<String, Object> eventCache = new ConcurrentHashMap<>();

  public SseEmitter save(String emitterId, SseEmitter sseEmitter) {
    emitters.put(emitterId, sseEmitter);
    return sseEmitter;
  }

  /**
   * 각 이벤트에 고유한 ID를 부여하여 저장한다.
   * <p>
   * 클라이언트가 놓친 이벤트를 복구할 수 있도록 대비하여 저장한다.
   **/
  public void saveEventCache(String eventCacheId, Object event) {
    eventCache.put(eventCacheId, event);
  }

  /**
   * 사용자당 n개의 연결을 허용할 수 있어야 한다. 라는 조건 충족
   **/
  public Map<String, SseEmitter> findAllEmitterByUserId(String userId) {
    return emitters.entrySet().stream()
        .filter(entry -> entry.getKey().startsWith(userId))
        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
  }

  public Map<String, Object> findAllEventCacheByUserId(String userId) {
    return eventCache.entrySet().stream()
        .filter(entry -> entry.getKey().startsWith(userId))
        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
  }

  public Map<String, SseEmitter> findAllEmitter() {
    return new ConcurrentHashMap<>(emitters); // 복사본으로 반환
  }

  public void deleteById(String emitterId) {
    emitters.remove(emitterId);
  }

  public void deleteAllEmitterByUserId(String userId) {
    emitters.forEach((key, emitter) -> {
      if (key.startsWith(userId)) {
        emitters.remove(key);
      }
    });
  }

  public void deleteAllEventCacheByUserId(String userId) {
    emitters.forEach((key, event) -> {
      if (key.startsWith(userId)) {
        emitters.remove(key);
      }
    });
  }
}
