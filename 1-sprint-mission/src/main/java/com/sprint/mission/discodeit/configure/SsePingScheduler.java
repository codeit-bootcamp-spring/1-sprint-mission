package com.sprint.mission.discodeit.configure;

import com.sprint.mission.discodeit.service.basic.SseEmitterService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class SsePingScheduler {

  private final SseEmitterService sseEmitterService;

  public SsePingScheduler(SseEmitterService sseEmitterService) {
    this.sseEmitterService = sseEmitterService;
  }

  @Scheduled(fixedRate = 30_000)
  public void sendPing() {
    sseEmitterService.sendPingToAll();
  }
}
