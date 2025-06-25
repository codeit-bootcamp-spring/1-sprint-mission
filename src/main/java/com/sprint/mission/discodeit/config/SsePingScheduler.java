package com.sprint.mission.discodeit.config;

import com.sprint.mission.discodeit.service.basic.SseEmitterService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SsePingScheduler {
    private final SseEmitterService sseService;

    @Scheduled(fixedRate = 1000 * 60)
    public void pingAllUsers() {
        sseService.sendPingToAll();
    }
}
