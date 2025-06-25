package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.service.SseService;
import com.sprint.mission.discodeit.sse.SseEvent;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Slf4j
@Service
public class BasicSseService implements SseService {

    private static final long SSE_CONNECTION_TIMEOUT_MS = 30 * 60 * 1000L;
    private static final long SSE_EVENT_REMOVE_INTERVAL_MS = 60 * 1000L;
    private static final long PING_INTERVAL_MS = 60 * 1000L;
    private static final long EVENT_RETENTION_MINUTES = 10;

    private final Map<UUID, Set<SseEmitter>> userConnections = new ConcurrentHashMap<>();
    private final Map<Long, SseEvent> eventStore = new ConcurrentSkipListMap<>();
    private final AtomicLong eventIdGenerator = new AtomicLong(0);

    @Override
    public SseEmitter createConnection(UUID userId, String lastEventId) {

        SseEmitter emitter = new SseEmitter(SSE_CONNECTION_TIMEOUT_MS);

        // 스레드 세이프한 set 반환하기
        userConnections.computeIfAbsent(userId, u -> ConcurrentHashMap.newKeySet()).add(emitter);

        emitter.onCompletion(() -> removeConnection(userId, emitter));
        emitter.onTimeout(() -> removeConnection(userId, emitter));
        emitter.onError((e) -> removeConnection(userId, emitter));

        try {
            emitter.send(SseEmitter.event()
                .id(eventIdGenerator.toString())
                .name("connected")
                .data("SSE 서비스에 연결되었습니다.")
            );
        } catch (IOException e) {
            log.info("SSE 연결 실패 (userId: {}, causeBy: {})", userId, e.getMessage());
            removeConnection(userId, emitter);
            emitter.completeWithError(e);
        }

        if (lastEventId != null && !lastEventId.isEmpty()) {
            restoreMissedEvents(userId, lastEventId, emitter);
        }

        log.info("SSE 연결 성공 (userId: {}, connection count: {})", userId,
            userConnections.get(userId).size());

        return emitter;
    }

    @Override
    public void sendToUser(String eventName, Object data, UUID userId) {
        SseEvent sseEvent = createEvent(eventName, data, userId);

        Set<SseEmitter> emitters = userConnections.get(userId);
        if (emitters == null || emitters.isEmpty()) {
            log.info("SSE 연결 없음 (userId: {})", userId);
            return;
        }

        Set<SseEmitter> deadEmitters = new HashSet<>();

        for (SseEmitter emitter : emitters) {
            try {
                emitter.send(SseEmitter.event()
                    .id(sseEvent.getId().toString())
                    .name(sseEvent.getName())
                    .data(sseEvent.getData())
                );
            } catch (IOException e) {
                log.info("SSE 연결 끊김 (userId: {}, causeBy: {})", userId, e.getMessage());
                deadEmitters.add(emitter);
            } catch (Exception e) {
                log.warn("SSE 전송 중 알 수 없는 예외 발생 (userId: {}, causeBy: {})", userId, e.getMessage());
                deadEmitters.add(emitter);
            }
        }

        emitters.removeAll(deadEmitters);
    }

    @Override
    public void sendToAll(String eventName, Object data) {
        SseEvent sseEvent = createEvent(eventName, data, null);

        userConnections.forEach((userId, emitters) -> {
            Set<SseEmitter> deadEmitters = new HashSet<>();

            for (SseEmitter emitter : emitters) {
                try {
                    emitter.send(SseEmitter.event()
                        .id(sseEvent.getId().toString())
                        .name(sseEvent.getName())
                        .data(sseEvent.getData())
                    );
                } catch (IOException e) {
                    log.info("SSE 연결 끊김 (userId: {}, causeBy: {})", userId, e.getMessage());
                    deadEmitters.add(emitter);
                } catch (Exception e) {
                    log.warn("SSE 전송 중 알 수 없는 예외 발생 (userId: {}, causeBy: {})", userId,
                        e.getMessage());
                    deadEmitters.add(emitter);
                }
            }

            emitters.removeAll(deadEmitters);
        });
    }

    @Scheduled(fixedDelay = PING_INTERVAL_MS)
    @Override
    public void sendBroadcastPing() {
        userConnections.forEach((userId, emitters) -> {
            Set<SseEmitter> deadEmitters = new HashSet<>();

            for (SseEmitter emitter : emitters) {
                try {
                    emitter.send(SseEmitter.event()
                        .name("ping")
                        .data("keep-alive")
                    );
                } catch (IOException e) {
                    log.debug("Ping 메세지 전송 실패 (userId: {}, causeBy: {})", userId, e.getMessage());
                    deadEmitters.add(emitter);
                } catch (Exception e) {
                    log.warn("SSE 전송 중 알 수 없는 예외 발생 (userId: {}, causeBy: {})", userId,
                        e.getMessage());
                    deadEmitters.add(emitter);
                }
            }

            emitters.removeAll(deadEmitters);
        });
    }

    @Scheduled(fixedDelay = SSE_EVENT_REMOVE_INTERVAL_MS)
    public void removeOldSseEvent() {
        Instant limitTime = Instant.now().minus(Duration.ofMinutes(EVENT_RETENTION_MINUTES));

        eventStore.entrySet().removeIf(entry ->
            entry.getValue().getCreatedAt().isBefore(limitTime)
        );
    }

    private void removeConnection(UUID userId, SseEmitter emitter) {
        Set<SseEmitter> emitters = userConnections.get(userId);
        if (emitters != null) {
            emitters.remove(emitter);
            if (emitters.isEmpty()) {
                userConnections.remove(userId);
            }
        }
    }

    private SseEvent createEvent(String eventName, Object data, UUID userId) {
        long eventId = eventIdGenerator.getAndIncrement();
        SseEvent sseEvent = new SseEvent(eventId, eventName, data, userId, Instant.now());
        eventStore.put(eventId, sseEvent);
        return sseEvent;
    }

    private void restoreMissedEvents(UUID userId, String lastEventId, SseEmitter emitter) {

        long lastId;
        try {
            lastId = Long.parseLong(lastEventId);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                "lastEventId 유효하지 않은 타입입니다. (lastEventId: " + lastEventId + ")", e);
        }

        // 유실된 이벤트 가져오기
        List<SseEvent> missedEvents = eventStore.values().stream()
            .filter(event -> event.getId() > lastId)
            .filter(event -> event.getUserId() == null || event.getUserId().equals(userId))
            .collect(Collectors.toList());

        try {
            for (SseEvent event : missedEvents) {
                emitter.send(SseEmitter.event()
                    .id(event.getId().toString())
                    .name(event.getName())
                    .data(event.getData())
                );
            }
        } catch (IOException e) {
            log.debug("SSE 연결 끊김 (userId: {}, causeBy: {})", userId, e.getMessage());
            removeConnection(userId, emitter);
        } catch (Exception e) {
            log.warn("SSE 전송 중 알 수 없는 예외 발생 (userId: {}, causeBy: {})", userId, e.getMessage());
            removeConnection(userId, emitter);
        }

        log.info("SSE 이벤트 복원 완료 (userId: {}, 복원된 이벤트 수: {})", userId, missedEvents.size());
    }


}
