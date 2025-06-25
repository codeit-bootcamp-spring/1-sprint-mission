package com.sprint.mission.discodeit.component;

import com.sprint.mission.discodeit.repository.EmitterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Component
@RequiredArgsConstructor
public class SseHeartbeatScheduler {

  private final EmitterRepository emitters;

  @Scheduled(fixedRate = 30_000)
  public void ping() {
    emitters.getStore().forEach((userId, list) -> list.forEach(emitter -> {
      try {
        emitter.send(SseEmitter.event()
            .comment("ping")
            .id("ping-" + System.nanoTime()));
      } catch (Exception e) {
        emitters.remove(userId, emitter);
      }
    }));
  }
}