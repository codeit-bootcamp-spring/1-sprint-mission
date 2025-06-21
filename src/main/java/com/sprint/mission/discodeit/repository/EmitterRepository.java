package com.sprint.mission.discodeit.repository;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Component
public class EmitterRepository {

  private final Map<UUID, List<SseEmitter>> emitters = new ConcurrentHashMap<>();

  public void add(UUID userId, SseEmitter emitter) {
    emitters.computeIfAbsent(userId, key -> new CopyOnWriteArrayList<>()).add(emitter);
  }

  public void remove(UUID userId, SseEmitter emitter) {
    List<SseEmitter> list = emitters.get(userId);
    if (list != null) {
      list.remove(emitter);
    }
  }

  public List<SseEmitter> get(UUID userId) {
    return emitters.getOrDefault(userId, List.of());
  }

  public Map<UUID, List<SseEmitter>> getAll() {
    return emitters;
  }

  public void sendChannelRefreshToUsers(List<UUID> userIds, UUID channelId) {
    for (UUID userId : userIds) {
      List<SseEmitter> userEmitters = emitters.get(userId);
      if (userEmitters == null) continue;

      for (SseEmitter emitter : userEmitters) {
        try {
          emitter.send(SseEmitter.event()
              .id(UUID.randomUUID().toString())
              .name("channels.refresh")
              .data(Map.of("channelId", channelId)));
        } catch (IOException e) {
          emitter.completeWithError(e);
        }
      }
    }
  }

  public void sendUserRefreshToUsers(List<UUID> userIds, UUID targetUserId) {
    userIds.forEach(userId -> {
      List<SseEmitter> userEmitters = emitters.get(userId);
      if (userEmitters != null) {
        String eventId = UUID.randomUUID().toString();
        Map<String, Object> data = Map.of("userId", targetUserId);
        userEmitters.forEach(emitter -> {
          try {
            emitter.send(SseEmitter.event()
                .id(eventId)
                .name("users.refresh")
                .data(data));
          } catch (IOException e) {
            emitter.completeWithError(e);
          }
        });
      }
    });
  }
}
