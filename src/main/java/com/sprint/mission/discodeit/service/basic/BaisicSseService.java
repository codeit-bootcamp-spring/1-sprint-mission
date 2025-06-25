package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import com.sprint.mission.discodeit.dto.data.NotificationDto;
import com.sprint.mission.discodeit.repository.EmitterRepository;
import com.sprint.mission.discodeit.service.SseService;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Service
@RequiredArgsConstructor
public class BaisicSseService implements SseService {
  private final EmitterRepository emitters;

  private final AtomicLong eventSeq = new AtomicLong();

  @Override
  public void sendInitEvent(SseEmitter emitter, String lastEventId) {
    try {
      emitter.send(SseEmitter.event()
          .id(String.valueOf(eventSeq.incrementAndGet()))
          .name("init")
          .data("connected"));
    } catch (IOException ignored) { }
  }

  @Override
  public void safeSend(SseEmitter emitter, SseEmitter.SseEventBuilder event, Long userId) {
    try {
      emitter.send(event);
    } catch (Exception ex) {
      emitters.remove(userId, emitter);
    }
  }

  private String nextId() {
    return String.valueOf(eventSeq.incrementAndGet());
  }

@Override
public void sendNotification(Long userId, NotificationDto dto) {
    List<SseEmitter> list = emitters.get(userId);
    for (SseEmitter emitter : list) {
      safeSend(emitter, SseEmitter.event()
              .id(nextId())
              .name("notifications")
              .data(dto),
          userId);
    }
  }

}
