package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import com.sprint.mission.discodeit.dto.data.NotificationDto;
import com.sprint.mission.discodeit.service.SshService;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Slf4j
@Service
@RequiredArgsConstructor
public class BasicSshService implements SshService {

  private static final long TIMEOUT = 30 * 60 * 1000L; // 30분

  private final Map<UUID, List<SseEmitter>> emitters = new ConcurrentHashMap<>();

  @Override
  public SseEmitter subscribe(UUID userId, UUID lastEventId) {
    SseEmitter emitter = new SseEmitter(TIMEOUT);
    emitters.computeIfAbsent(userId, k -> new CopyOnWriteArrayList<>()).add(emitter);

    emitter.onCompletion(() -> removeEmitter(userId, emitter));
    emitter.onTimeout(() -> removeEmitter(userId, emitter));
    emitter.onError((e) -> removeEmitter(userId, emitter));

    try {
      emitter.send(SseEmitter.event()
          .name("connected")
          .data("SSE 연결에 성공했습니다.")
          .id(UUID.randomUUID().toString()));
    } catch (IOException e) {
      removeEmitter(userId, emitter);
      emitter.completeWithError(e);
    }

    return emitter;
  }

  private void removeEmitter(UUID userId, SseEmitter emitter) {
    List<SseEmitter> list = emitters.get(userId);
    if (list != null) {
      list.remove(emitter);
    }
  }

  @Override
  public void sendNotification(UUID userId, NotificationDto dto) {
    broadcast(userId, "notification", dto);
  }

  @Override
  public void sendBinaryContentStatus(UUID userId, BinaryContentDto dto) {
    broadcast(userId, "binaryContentStatus", dto);
  }

  @Override
  public void sendChannelRefresh(UUID userId, UUID channelId) {
    broadcast(userId, "channelRefresh", channelId);
  }

  @Override
  public void sendUserRefresh(UUID userId) {
    broadcast(userId, "userRefresh", null);
  }

  @Override
  public void broadcast(UUID userId, String eventName, Object data) {
    List<SseEmitter> list = emitters.getOrDefault(userId, List.of());
    list.forEach(emitter -> sendToEmitter(emitter, eventName, data));
  }

  private void sendToEmitter(SseEmitter emitter, String eventName, Object data) {
    try {
      emitter.send(SseEmitter.event()
          .id(UUID.randomUUID().toString())
          .name(eventName)
          .data(data));
    } catch (IOException e) {
      emitter.completeWithError(e);
    }
  }

  @Override
  @Scheduled
  public void ping() {
    emitters.forEach((userId, list) ->
        list.removeIf(emitter -> {
          try {
            emitter.send(SseEmitter.event()
                .id(UUID.randomUUID().toString())
                .name("ping")
                .data(""));
            return false;
          } catch (Exception e) {
            emitter.complete();
            return true;
          }
        })
    );
  }
}
