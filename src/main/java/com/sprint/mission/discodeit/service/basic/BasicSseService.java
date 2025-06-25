package com.sprint.mission.discodeit.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import com.sprint.mission.discodeit.dto.data.NotificationDto;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Slf4j
@Service
@RequiredArgsConstructor
public class BasicSseService implements SseService {

  private static final Long DEFAULT_TIMEOUT = 60L * 60 * 1000; // 1시간
  private static final Long PING_INTERVAL = 30L; // 30초

  private final ObjectMapper objectMapper;

  // 사용자별 다중 연결을 관리하기 위한 스레드 세이프한 구조
  private final Map<UUID, CopyOnWriteArrayList<SseConnection>> userConnections = new ConcurrentHashMap<>();

  // 이벤트 ID 생성을 위한 카운터
  private final AtomicLong eventIdCounter = new AtomicLong(0);

  // Ping 전송을 위한 스케줄러
  private ScheduledExecutorService scheduler;

  @PostConstruct
  public void init() {
    // Ping 스케줄러 초기화
    scheduler = Executors.newScheduledThreadPool(1);
    scheduler.scheduleWithFixedDelay(this::sendPingToAll,
        PING_INTERVAL, PING_INTERVAL, TimeUnit.SECONDS);
  }

  @PreDestroy
  public void destroy() {
    // 애플리케이션 종료 시 스케줄러 정리
    if (scheduler != null && !scheduler.isShutdown()) {
      scheduler.shutdown();
    }
  }

  public SseEmitter createConnection(UUID userId, String lastEventId) {
    SseEmitter emitter = new SseEmitter(DEFAULT_TIMEOUT);
    String connectionId = UUID.randomUUID().toString();

    SseConnection connection = new SseConnection(connectionId, userId, emitter);

    // 연결 완료 시 제거
    emitter.onCompletion(() -> removeConnection(userId, connectionId));

    // 타임아웃 시 제거
    emitter.onTimeout(() -> {
      log.warn("SSE 연결 타임아웃: userId={}, connectionId={}", userId, connectionId);
      removeConnection(userId, connectionId);
    });

    // 에러 발생 시 제거
    emitter.onError(throwable -> {
      log.error("SSE 연결 에러: userId={}, connectionId={}", userId, connectionId, throwable);
      removeConnection(userId, connectionId);
    });

    // 사용자별 연결 목록에 추가
    userConnections.computeIfAbsent(userId, k -> new CopyOnWriteArrayList<>())
        .add(connection);

    // 초기 연결 이벤트 전송
    sendInitialEvent(connection);

    // Last-Event-ID가 있으면 놓친 이벤트 복원 (실제 구현 시 이벤트 저장소 필요)
    if (lastEventId != null) {
      restoreMissedEvents(connection, lastEventId);
    }

    log.info("SSE 연결 생성: userId={}, connectionId={}", userId, connectionId);

    return emitter;
  }

  // 새로운 알림 이벤트 전송
  public void sendNotificationEvent(UUID userId, NotificationDto notification) {
    String eventId = String.valueOf(eventIdCounter.incrementAndGet());
    sendEventToUser(userId, "notifications", notification, eventId);
  }

  // 파일 업로드 상태 변경 이벤트 전송
  public void sendBinaryContentStatusEvent(UUID userId, BinaryContentDto binaryContent) {
    String eventId = String.valueOf(eventIdCounter.incrementAndGet());
    sendEventToUser(userId, "binaryContents.status", binaryContent, eventId);
  }

  // 채널 목록 갱신 이벤트 전송
  public void sendChannelRefreshEvent(UUID userId, UUID channelId) {
    String eventId = String.valueOf(eventIdCounter.incrementAndGet());
    Map<String, Object> data = Map.of("channelId", channelId.toString());
    sendEventToUser(userId, "channels.refresh", data, eventId);
  }

  // 사용자 목록 갱신 이벤트 전송
  public void sendUserRefreshEvent(UUID userId, UUID targetUserId) {
    String eventId = String.valueOf(eventIdCounter.incrementAndGet());
    Map<String, Object> data = Map.of("userId", targetUserId.toString());
    sendEventToUser(userId, "users.refresh", data, eventId);
  }

  // 특정 사용자의 모든 연결에 이벤트 전송
  private void sendEventToUser(UUID userId, String eventName, Object data, String eventId) {
    CopyOnWriteArrayList<SseConnection> connections = userConnections.get(userId);
    if (connections == null || connections.isEmpty()) {
      return;
    }

    try {
      String jsonData = objectMapper.writeValueAsString(data);

      for (SseConnection connection : connections) {
        try {
          SseEmitter.SseEventBuilder event = SseEmitter.event()
              .id(eventId)
              .name(eventName)
              .data(jsonData);

          connection.getEmitter().send(event);
          connection.updateLastEventId(eventId);

        } catch (IOException e) {
          log.error("SSE 이벤트 전송 실패: connectionId={}", connection.getId(), e);
          removeConnection(userId, connection.getId());
        }
      }
    } catch (Exception e) {
      log.error("SSE 이벤트 직렬화 실패", e);
    }
  }

  // Ping 전송
  private void sendPingToAll() {
    userConnections.forEach((userId, connections) -> {
      connections.forEach(connection -> {
        try {
          connection.getEmitter().send(SseEmitter.event()
              .comment("ping"));
          connection.updateLastPing();
        } catch (IOException e) {
          log.debug("Ping 전송 실패, 연결 제거: userId={}, connectionId={}",
              userId, connection.getId());
          removeConnection(userId, connection.getId());
        }
      });
    });

    // 오래된 연결 정리 (마지막 ping으로부터 2분 이상 경과)
    cleanupStaleConnections();
  }

  // 오래된 연결 정리
  private void cleanupStaleConnections() {
    long staleThreshold = System.currentTimeMillis() - Duration.ofMinutes(2).toMillis();

    userConnections.forEach((userId, connections) -> {
      connections.removeIf(connection -> {
        if (connection.getLastPingTime() < staleThreshold) {
          try {
            connection.getEmitter().complete();
          } catch (Exception e) {
            // 무시
          }
          log.info("오래된 연결 제거: userId={}, connectionId={}",
              userId, connection.getId());
          return true;
        }
        return false;
      });
    });
  }

  // 연결 제거
  private void removeConnection(UUID userId, String connectionId) {
    CopyOnWriteArrayList<SseConnection> connections = userConnections.get(userId);
    if (connections != null) {
      connections.removeIf(conn -> conn.getId().equals(connectionId));

      // 사용자의 모든 연결이 끊어진 경우 맵에서 제거
      if (connections.isEmpty()) {
        userConnections.remove(userId);
      }
    }
  }

  // 초기 연결 이벤트
  private void sendInitialEvent(SseConnection connection) {
    try {
      connection.getEmitter().send(SseEmitter.event()
          .comment("connected"));
    } catch (IOException e) {
      log.error("초기 이벤트 전송 실패", e);
    }
  }

  // 놓친 이벤트 복원 (실제 구현 시 이벤트 저장소 필요)
  private void restoreMissedEvents(SseConnection connection, String lastEventId) {
    // TODO: 이벤트 저장소에서 lastEventId 이후의 이벤트를 조회하여 전송
    log.info("놓친 이벤트 복원: lastEventId={}", lastEventId);
  }

  // SSE 연결 정보를 담는 내부 클래스
  private static class SseConnection {

    private final String id;
    private final UUID userId;
    private final SseEmitter emitter;
    private volatile String lastEventId;
    private volatile long lastPingTime;

    public SseConnection(String id, UUID userId, SseEmitter emitter) {
      this.id = id;
      this.userId = userId;
      this.emitter = emitter;
      this.lastPingTime = System.currentTimeMillis();
    }

    public String getId() {
      return id;
    }

    public UUID getUserId() {
      return userId;
    }

    public SseEmitter getEmitter() {
      return emitter;
    }

    public String getLastEventId() {
      return lastEventId;
    }

    public void updateLastEventId(String eventId) {
      this.lastEventId = eventId;
    }

    public long getLastPingTime() {
      return lastPingTime;
    }

    public void updateLastPing() {
      this.lastPingTime = System.currentTimeMillis();
    }
  }
}