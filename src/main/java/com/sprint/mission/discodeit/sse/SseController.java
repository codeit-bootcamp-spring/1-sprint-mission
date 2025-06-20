package com.sprint.mission.discodeit.sse;

import com.sprint.mission.discodeit.security.CustomUserDetails;
import java.io.IOException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequiredArgsConstructor
public class SseController {

    private final SseEmitterRepository sseEmitterRepository;
    private final SseEventCacheRepository sseEventCacheRepository;

    @GetMapping(value = "/api/sse", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter connect(@AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestHeader(value = "Last-Event-ID", required = false) String lastEventId) {
        String emitterId = UUID.randomUUID().toString();
        SseEmitter sseEmitter = new SseEmitter(60_000L);
        UUID userId = userDetails.getId();

        sseEmitterRepository.add(userId, emitterId, sseEmitter);

        sseEmitter.onCompletion(() -> sseEmitterRepository.remove(userId, emitterId));
        sseEmitter.onTimeout(() -> sseEmitterRepository.remove(userId, emitterId));
        sseEmitter.onError(e -> sseEmitterRepository.remove(userId, emitterId));

        try {
            if (lastEventId != null) {
                sseEventCacheRepository.getEventsAfter(userId, lastEventId).values()
                        .forEach(event -> {
                            try {
                                sseEmitter.send(event);
                            } catch (IOException e) {
                                sseEmitter.completeWithError(e);
                            }
                        });
            }
            sseEmitter.send(SseEmitter.event().id(emitterId).name("connect").data("connected"));
        } catch (IOException e) {
            sseEmitter.completeWithError(e);
            return sseEmitter;
        }
        return sseEmitter;
    }
}
