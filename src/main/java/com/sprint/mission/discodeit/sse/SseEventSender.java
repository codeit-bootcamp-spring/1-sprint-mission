package com.sprint.mission.discodeit.sse;

import com.sprint.mission.discodeit.dto.binary.BinaryContentDto;
import com.sprint.mission.discodeit.dto.notification.NotificationDto;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter.SseEventBuilder;

@Component
@RequiredArgsConstructor
public class SseEventSender {

    private final SseEmitterRepository sseEmitterRepository;
    private final SseEventCacheRepository sseEventCacheRepository;

    public void sendNotification(UUID userId, NotificationDto dto) {
        send(userId, "notifications", dto);
    }

    public void sendBinaryStatus(UUID userId, BinaryContentDto dto) {
        send(userId, "binaryContents.status", dto);
    }

    public void sendChannelRefresh(UUID userId, UUID channelId) {
        send(userId, "channels.refresh", Map.of("channelId", channelId));
    }

    public void sendUserRefresh(UUID userId, UUID targetUserId) {
        send(userId, "users.refresh", Map.of("userId", targetUserId));
    }

    private void send(UUID userId, String eventName, Object data) {
        String eventId = UUID.randomUUID().toString();
        SseEventBuilder event = SseEmitter.event()
                .id(eventId)
                .name(eventName).data(data);

        sseEventCacheRepository.save(userId, eventId, event);

        sseEmitterRepository.getEmitters(userId).forEach(emitter -> {
            try {
                emitter.send(SseEmitter.event()
                        .id(UUID.randomUUID().toString())
                        .name(eventName)
                        .data(data));
            } catch (IOException e) {
                emitter.completeWithError(e);
            }
        });
    }
}
