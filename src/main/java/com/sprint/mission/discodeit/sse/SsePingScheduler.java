package com.sprint.mission.discodeit.sse;

import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Component
@RequiredArgsConstructor
public class SsePingScheduler {

    private final SseEmitterRepository sseEmitterRepository;

    @Scheduled(fixedRate = 30_000)
    public void ssePing() {
        sseEmitterRepository.getAll().forEach((userId, emitterMap) -> {
            emitterMap.forEach((emitterId, emitter) -> {
                try {
                    emitter.send(SseEmitter.event()
                            .id("ping")
                            .data("keep-alive"));
                } catch (IOException e) {
                    sseEmitterRepository.remove(userId, emitterId);
                }
            });
        });
    }
}
