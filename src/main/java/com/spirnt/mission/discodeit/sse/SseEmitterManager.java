package com.spirnt.mission.discodeit.sse;

import com.spirnt.mission.discodeit.dto.binaryContent.BinaryContentDto;
import com.spirnt.mission.discodeit.dto.notification.NotificationDto;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Component
@Slf4j
public class SseEmitterManager {

    // 사용자별 SSE 연결(SseEmitter) 관리하는 맵
    // ConcurrentHashMap으로 스레드 세이프한 메모리 구조에서 관리
    // N개의 연결 허용하기 위해 SseEmitter를 List로 관리
    private final Map<UUID, List<SseEmitter>> userConnections = new ConcurrentHashMap<>();

    // 사용자별로 보낸 이벤트를 저장
    private final Map<UUID, List<SseEvent>> eventCache = new ConcurrentHashMap<>();

    @AllArgsConstructor
    public static class SseEvent {

        String id;
        String name;
        Object data;
        Instant createdAt;

    }

    /**
     * SSE 연결 = 알림 구독 사용자의 ID를 기준으로 그 사용자를 위한 SSE 연결 통로(SseEmitter) 생성
     */
    public SseEmitter subscribe(UUID userId, String lastEventId) {

        // SSE 생성 - 타임아웃 30분
        SseEmitter newEmitter = new SseEmitter(30 * 60 * 1000L);
        // 맵 업데이트
        /**
         * 새롭게 알게 된 점.
         * sse는 멀티스레드 환경에서 작동
         * -> 일반 new ArrayList를 생성하면 ConcurrentModificationException 발생
         * -> CopyWriteArrayList로 읽기 작업은 락 없이 빠르게, 쓰기는 복사 해서 안전하게 처리
         */
        List<SseEmitter> sseEmitters = userConnections.computeIfAbsent(userId,
            key -> new CopyOnWriteArrayList<>());   // 맵에 userId 키가 존재하면 반환하고, 없으면 새로운 빈 리스트 반환
        sseEmitters.add(newEmitter);

        // 메모리 누수 방지
        newEmitter.onCompletion(() -> removeEmitter(userId, newEmitter)); // 클라이언트가 정상적으로 연결을 종료했을 때
        newEmitter.onTimeout(
            () -> removeEmitter(userId, newEmitter));    // SseEmitter가 타임아웃되어 연결이 자동 종료될 때
        newEmitter.onError((e) -> removeEmitter(userId, newEmitter)); // 에러

        // 연결 확인용 메시지 전송
        try {
            newEmitter.send(SseEmitter.event()
                .name("Connected")
                .id(UUID.randomUUID().toString())
                .data("Sse Connection Success"));
        } catch (IOException e) {
            newEmitter.completeWithError(e);
        }

        // 이벤트 유실 복원
        if (lastEventId != null) {
            List<SseEvent> events = eventCache.getOrDefault(userId, List.of());

            // 해당 ID에 해당하는 이벤트의 createdAt 찾기
            Optional<Instant> lastTime = events.stream()
                .filter(e -> e.id.equals(lastEventId))
                .map(e -> e.createdAt)
                .findFirst();

            // 그 이후의 이벤트를 다시 전송
            if (lastTime.isPresent()) {
                for (SseEvent event : events) {
                    if (event.createdAt.isAfter(lastTime.get())) {
                        try {
                            newEmitter.send(SseEmitter.event()
                                .id(event.id)
                                .name(event.name)
                                .data(event.data));
                        } catch (IOException e) {
                            newEmitter.completeWithError(e);
                        }

                    }
                }
            }
        }

        return newEmitter;
    }

    private void removeEmitter(UUID userId, SseEmitter emitter) {
        List<SseEmitter> sseEmitters = userConnections.get(userId);
        if (sseEmitters == null) {
            return;
        }

        sseEmitters.remove(emitter);    // 연결 리스트에서 삭제
        if (sseEmitters.isEmpty()) {
            userConnections.remove(userId); // 연결 리스트가 비어있으면 맵에서 삭제
        }
    }

    // 주기적으로 ping 보내서 응답이 없는 연결 정리
    @Scheduled(fixedRate = 30000)   // 30초마다 ping 전송
    private void sendPingToAllConnections() {
        userConnections.keySet().stream()
            .forEach(userId -> {
                List<SseEmitter> emitters = userConnections.get(userId);
                if (emitters == null) {
                    return;
                }

                for (SseEmitter emitter : emitters) {
                    try {
                        emitter.send(SseEmitter.event()
                            .name("ping")
                            .data("ping"));
                    } catch (IOException e) {
                        emitter.completeWithError(e);
                        // 실패 = 응답 없는 emitter 정리
                        removeEmitter(userId, emitter);
                    }
                }
            });
    }

    public void send(UUID userId, String name, Object data) {
        List<SseEmitter> emitters = userConnections.get(userId);
        if (emitters == null || emitters.isEmpty()) {
            return;
        }

        String eventId = UUID.randomUUID().toString();
        Instant createdAt = Instant.now();
        // 전송 이벤트를 캐시에 기록
        eventCache.computeIfAbsent(userId, k -> new CopyOnWriteArrayList<>())
            .add(new SseEvent(eventId, name, data, createdAt));

        for (SseEmitter emitter : new ArrayList<>(emitters)) {
            try {
                emitter.send(SseEmitter.event()
                    .id(UUID.randomUUID().toString())
                    .name(name)
                    .data(data));
                log.info("Sending Sse Success: {}", name);
            } catch (IOException e) {
                log.warn("Sending Sse Failed: {}", name);
                emitter.completeWithError(e);
                removeEmitter(userId, emitter);
            }
        }
    }

    // 새로운 Notification 생긴 경우 클라이언트에게 알림 전송
    public void sendNotification(UUID userId, NotificationDto notificationDto) {
        send(userId, "notifications", notificationDto);
    }

    // 파일 업로드 상태 변경 시 전송
    public void sendFileUploadStatus(UUID userId, BinaryContentDto binaryContentDto) {
        send(userId, "binaryContents.status", binaryContentDto);
    }

    // 채널 목록 갱신 이벤트 전송
    // 채널 내 참가자에게만 전송
    public void sendChannelRefreshEvent(List<UUID> userIds, UUID channelId) {
        for (UUID userId : userIds) {
            send(userId, "channels.refresh", channelId);
        }
    }

    // 사용자 목록 갱신 이벤트 전송
    // 모든 유저에게 전송
    public void sendUsersRefreshEvent(UUID changedUserId) {
        for (UUID userId : userConnections.keySet()) {
            send(userId, "users.refresh", changedUserId);
        }
    }
}
