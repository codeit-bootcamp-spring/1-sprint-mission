package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentDto;
import com.sprint.mission.discodeit.dto.notification.NotificationDto;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
@RequiredArgsConstructor
public class SseService {

    // 사용자 ID → SseEmitter 리스트 (스레드 세이프)
    private final Map<UUID, CopyOnWriteArrayList<SseEmitter>> emitters = new ConcurrentHashMap<>();

    public SseEmitter subscribe(UUID userId, String lastEventId) {
        SseEmitter emitter = new SseEmitter(60_000L); // 60초 타임아웃

        emitters.computeIfAbsent(userId, id -> new CopyOnWriteArrayList<>()).add(emitter);

        emitter.onCompletion(() -> removeEmitter(userId, emitter));
        emitter.onTimeout(() -> removeEmitter(userId, emitter));
        emitter.onError(e -> removeEmitter(userId, emitter));

        try {
            emitter.send(SseEmitter.event()
                    .id("init-" + System.currentTimeMillis())
                    .name("sse-init")
                    .data("connected"));
        } catch (IOException ignored) {}

        return emitter;
    }

    private void removeEmitter(UUID userId, SseEmitter emitter) {
        List<SseEmitter> list = emitters.get(userId);
        if (list != null) {
            list.remove(emitter);
        }
    }

    @Scheduled(fixedDelay = 30_000)
    public void sendPing() {
        emitters.forEach((userId, emitterList) -> {
            for (SseEmitter emitter : emitterList) {
                try {
                    emitter.send(SseEmitter.event()
                            .id("ping-" + System.currentTimeMillis())
                            .name("ping")
                            .data("keep-alive"));
                } catch (Exception e) {
                    removeEmitter(userId, emitter);
                }
            }
        });
    }

    public void sendNotification(UUID userId, NotificationDto dto) {
        send(userId, "notifications", dto, dto.id().toString());
    }

    public void sendBinaryStatus(UUID userId, BinaryContentDto dto) {
        send(userId, "binaryContents.status", dto, dto.getId().toString());
    }

    public void sendChannelRefresh(UUID userId, UUID channelId) {
        send(userId, "channels.refresh", Map.of("channelId", channelId), channelId.toString());
    }

    public void sendUserRefresh(UUID userId) {
        send(userId, "users.refresh", Map.of("userId", userId), userId.toString());
    }

    public void sendUserRefreshToAll(Collection<UUID> userIds) {
        userIds.forEach(this::sendUserRefresh);
    }

    private void send(UUID userId, String eventName, Object data, String id) {
        List<SseEmitter> userEmitters = emitters.getOrDefault(userId, new CopyOnWriteArrayList<>());
        for (SseEmitter emitter : new ArrayList<>(userEmitters)) {
            try {
                emitter.send(SseEmitter.event()
                        .id(id)
                        .name(eventName)
                        .data(data));
            } catch (Exception e) {
                removeEmitter(userId, emitter);
            }
        }
    }
}