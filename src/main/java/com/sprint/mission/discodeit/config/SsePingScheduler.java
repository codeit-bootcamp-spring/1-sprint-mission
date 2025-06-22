package com.sprint.mission.discodeit.config;

import com.sprint.mission.discodeit.repository.SseEmitterRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SsePingScheduler {

  private final SseEmitterRepository emitterRepository;

  @Scheduled(fixedDelay = 30000) // 30초마다
  public void sendPingToAll() {
    log.debug("SSE ping 전송 시작");
    emitterRepository.getAllUserIds().forEach(userId -> {
      emitterRepository.send(userId, "ping", "pong");
    });
  }
}

