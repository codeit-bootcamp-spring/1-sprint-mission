package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.NotificationDto;
import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentDto;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Slf4j
@Service
public class SseService {

  // 사용자별 SSE 연결 관리 (사용자당 N개 연결 허용)
  // ConcurrentHashMap은 동시 접근을 안전하게 처리
  private final Map<UUID, Set<SseEmitterWrapper>> userConnections = new ConcurrentHashMap<>();
  //여러 사용자가 동시에 SSE 연결/해제하는 멀티스레드 환경에서 HashMap을 사용하면 데이터 손상이 일어날 수 있기 때문

  // 이벤트별 고유 ID 생성용
  private final AtomicLong eventIdGenerator = new AtomicLong(1);

  // 이벤트 저장소 (Last-Event-ID 복원용)
  private final Map<String, SseEventData> eventStore = new ConcurrentHashMap<>();

  // 최대 이벤트 저장 개수 (메모리 관리)
  private static final int MAX_EVENT_STORE_SIZE = 1000;

  // 유저별 최대 연결 개수
  private static final int MAX_CONNECTIONS_PER_USER = 5;


  public SseEmitter subscribe(UUID userId, String lastEventId) {
    Set<SseEmitterWrapper> existingConnections = userConnections.get(userId);

    // 최대 연결수 체크
    if (existingConnections != null && existingConnections.size() >= MAX_CONNECTIONS_PER_USER) {
      log.warn("사용자 최대 연결수 초과: userId = {}, 현재 연결수 = {}",
          userId, existingConnections.size());
      throw new IllegalStateException("최대 연결수를 초과했습니다.");
    }

    SseEmitter emitter = new SseEmitter(30 * 60 * 1000L); // 30분 타임아웃 설정
    String connectionId = UUID.randomUUID().toString();

    SseEmitterWrapper wrapper = new SseEmitterWrapper(emitter, userId, connectionId);

    //사용자별 연결 목록에 추가
    userConnections.computeIfAbsent(userId, s -> new HashSet<>()).add(wrapper);
    //computeIfAbsent: 키가 없거나 null이면 값을 계산해서 넣고, 있으면 기존 값을 반환하는 메서드

    log.info("SSE 연결 생성: 사용자 ID = {}, 연결 ID = {}, 현재 연결 수 = {}",
        userId, connectionId, userConnections.get(userId).size());

    // 이벤트 핸들러 설정(메모리 누수 방지)
    emitter.onCompletion(() -> {
      log.info("클라이언트에서 SSE 연결 해제: userId = {}, connectionId = {}", userId, connectionId);
      removeConnection(userId, wrapper);
    });
    emitter.onTimeout(() -> {
      log.info("SSE 연결 TIME OUT: userID = {}, connectionId = {}", userId, connectionId);
      removeConnection(userId, wrapper);
    });
    emitter.onError(throwable -> {
      log.error("SSE 연결 오류: 사용자 ID = {}, 연결 ID = {}", userId, wrapper.getConnectionId());
      removeConnection(userId, wrapper);
    });

    try {
      // 연결 확인 메세지 전송
      emitter.send(SseEmitter.event()
          .id(String.valueOf(eventIdGenerator.getAndIncrement()))
          .name("connect")
          .data("SSE 연결 성공"));

      // Last-Event-ID가 있으면 이벤트가 누락된 것이므로 이벤트 재전송
      if (lastEventId != null && !lastEventId.isEmpty()) {
        resendMissedEvents(emitter, lastEventId);
      }

    } catch (IOException e) {
      log.error("SSE 연결 확인 메세지 전송 실패", e);
      removeConnection(userId, wrapper);
    }
    return emitter;
  }

  // Ping을 주기적으로 보내고 죽은 연결을 정리
  @Scheduled(fixedRate = 30000) // 30초마다 실행
  public void pingAndCleanup() {
    log.debug("SSE 연결 상태 확인 및 정리 시작");

    userConnections.forEach((userId, connections) -> {
      List<SseEmitterWrapper> connectionList = new ArrayList<>(connections);

      for (SseEmitterWrapper wrapper : connectionList) {
        try {
          wrapper.getEmitter().send(SseEmitter.event()
              .name("ping")
              .data("ping"));
        } catch (IOException e) {
          log.debug("Ping 실패로 연결 제거: 사용자 ID = {}, 연결 ID = {}", userId, wrapper.getConnectionId());
          removeConnection(userId, wrapper);
        }
      }
    });

    // 비어있는 사용자 엔트리 정리
    userConnections.entrySet().removeIf(entry -> entry.getValue().isEmpty());

    log.debug("SSE 연결 상태 확인 완료. 활성 사용자 수: {}", userConnections.size());
  }


  // 특정 사용자에게 이벤트 발송
  public void sendToUser(UUID userId, String eventName, Object data) {
    Set<SseEmitterWrapper> connections = userConnections.get(userId);
    if (connections == null || connections.isEmpty()) {
      log.debug("이 사용자에게 전송할 SSE 연결이 없음: 사용자 ID = {} ", userId);
      return;
    }

    String eventId = String.valueOf(eventIdGenerator.getAndIncrement());

    // 복원용 이벤트 저장
    storeEvent(eventId, eventName, data);

    log.info("사용자에게 이벤트 전송: 사용자 ID = {}, 이벤트명 = {}, 연결 수 = {}", userId, eventId,
        connections.size());

    // 모든 연결에 이벤트 전송
    List<SseEmitterWrapper> connectionsList = new ArrayList<>(connections);
    for (SseEmitterWrapper wrapper : connectionsList) {
      try {
        wrapper.getEmitter().send(
            SseEmitter.event()
                .id(eventId)
                .name(eventName)
                .data(data));
        log.debug("이벤트 전송 완료:  사용자 ID = {}, 이벤트명 = {}", userId, eventId);
      } catch (IOException e) {
        log.debug("SSE 전송 실패 (정상): {}", e.getMessage());
        removeConnection(userId, wrapper);
      } catch (Exception e) {
        log.error("예상치 못한 SSE 에러", e);
        removeConnection(userId, wrapper);
      }
    }
  }

  //모든 사용자에게 이벤트 전송
  public void sendToAll(String eventName, Object data) {
    userConnections.keySet().forEach(userId -> sendToUser(userId, eventName, data));
  }

  // 알림 이벤트 전송
  public void sendNotification(UUID userId, NotificationDto notification) {
    log.info("알림 이벤트 발송: userId= {}, notificationId = {}", userId, notification.id());
    sendToUser(userId, "notifications", notification);
  }

  // 파일 업로드 상태 변경 이벤트 전송
  public void sendBinaryContentStatus(BinaryContentDto binaryContent) {
    log.info("파일 업로드 상태 변경 이벤트 발송:  binaryContent = {}", binaryContent);
    sendToAll("binaryContents.status", binaryContent);
    //todo
    // - 모두에게 전송할 필요가 있을까? 해당 채널에 있는 사람에게만 보내도 되지 않을까?
    // - 공개채널의 경우에는 모두에게 보내고
    // - 비공개채널의 경우엔 userId리스트를 받아서 해당 채널에 속하는 유저들에게만 보내면 되지 않을까?
    // but how?
  }

  // 채널 목록 변경 이벤트 전송
  public void sendChannelRefresh(UUID channelId) {
    log.info("채널 목록 변경 이벤트 모든 사용자에게 발송: channelId = {}", channelId);
    Map<String, Object> data = Map.of("channelId", channelId);
    sendToAll("channels.refresh", data);
  }

  public void sendChannelRefresh(UUID channelId, List<UUID> userIds) {
    log.info("채널 목록 변경 이벤트 발송: channelId = {}, userIds = {}", channelId, userIds);
    Map<String, Object> data = Map.of("channelId", channelId);
    for (UUID userId : userIds) {
      sendToUser(userId, "channels.refresh", data);
    }
  }

  public void sendUserRefresh(UUID newUserId) {
    Map<String, Object> data = Map.of("userId", newUserId);
    sendToAll("users.refresh", data);
  }

  // Last-Event-ID 복원을 위한 이벤트 저장
  public void storeEvent(String eventId, String eventName, Object data) {
    eventStore.put(eventId, new SseEventData(eventId, eventName, data, System.currentTimeMillis()));

    //이벤트 저장소 크기 제한
    if (eventStore.size() > MAX_EVENT_STORE_SIZE) {
      //가장 오래된 이벤트 제거
      eventStore.values().stream()
          .min(Comparator.comparing(SseEventData::getTimestamp))
          .map(SseEventData::getEventId)
          .ifPresent(eventStore::remove);
    }
  }

  // 누락된 이벤트 재전송
  private void resendMissedEvents(SseEmitter emitter, String lastEventId) {
    try {
      long lastId = Long.parseLong(lastEventId);

      eventStore.values().stream()
          //마지막으로 전송된 이벤트보다 id값이 큰 것들만 (= 누락된 것들)
          .filter(event -> Long.parseLong(event.getEventId()) > lastId)
          //이벤트 ID 순으로 정렬
          .sorted(Comparator.comparing(event -> Long.parseLong(event.getEventId())))
          .forEach(event -> {
            try {
              emitter.send(SseEmitter.event()
                  .id(event.getEventId())
                  .name(event.getEventName())
                  .data(event.getData()));

            } catch (Exception e) {
              log.error("누락된 이벤트 재전송 실패", e);
            }
          });
    } catch (NumberFormatException e) {
      log.warn("잘못된 Last-Event-ID 형식: {}", lastEventId);
    }
  }

  // 연결 제거
  private void removeConnection(UUID userId, SseEmitterWrapper wrapper) {
    Set<SseEmitterWrapper> connections = userConnections.get(userId);
    if (connections != null) {
      connections.remove(wrapper);
      if (connections.isEmpty()) {
        userConnections.remove(userId);
      }
    }
    log.info("SSE 연결 제거: 사용자 ID = {}, 연결 ID = {}", userId, wrapper.getConnectionId());
  }


  // SSE Emitter 래퍼 클래스
  @Data
  @AllArgsConstructor
  private static class SseEmitterWrapper {

    private final SseEmitter emitter;
    private final UUID userId;
    private final String connectionId;

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (o == null || getClass() != o.getClass()) {
        return false;
      }
      SseEmitterWrapper that = (SseEmitterWrapper) o;
      return Objects.equals(connectionId, that.connectionId);
    }

    @Override
    public int hashCode() {
      return Objects.hash(connectionId);
    }
  }

  // 이벤트 데이터 저장용 클래스
  @Data
  @AllArgsConstructor
  private static class SseEventData {

    private final String eventId;
    private final String eventName;
    private final Object data;
    private final long timestamp;
  }

}
