package com.sprint.mission.discodeit.service.notification;

import com.sprint.mission.discodeit.dto.response.NotificationDto;
import com.sprint.mission.discodeit.dto.response.SseEvent;
import com.sprint.mission.discodeit.dto.response.UserDto;
import com.sprint.mission.discodeit.entity.notification.Notification;
import com.sprint.mission.discodeit.entity.notification.NotificationType;
import com.sprint.mission.discodeit.entity.status.read.ReadStatus;
import com.sprint.mission.discodeit.entity.user.User;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.repository.NotificationRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter.SseEventBuilder;

@Slf4j
@Service
//@Transactional
@EnableScheduling
@RequiredArgsConstructor
public class NotificationService {

  private final static int MAX_QUEUE_SIZE = 100;

  private final Map<UUID, Set<SseEmitter>> userConnections = new ConcurrentHashMap<>(); // emitter 연결
  private final Map<UUID, Deque<SseEvent>> events = new ConcurrentHashMap<>(); // 이벤트 히스토리

  private final NotificationRepository notificationRepository;
  private final ReadStatusRepository readStatusRepository;
  private final UserRepository userRepository;

  /**
   * @methodName : emitterClean
   * @date : 2025. 6. 16. 17:35
   * @author : wongil
   * @Description: 응답 안하는 SSE 연결 해제
   **/
  @Scheduled(fixedRate = 5000)
  protected void emitterClean() {
    userConnections
        .forEach((userId, emitters) -> {
          emitters
              .forEach(emitter -> {
                try {
                  emitter.send(SseEmitter.event()
                      .name("ping")
                      .data("pong"));
                  log.info("SSE 연결 해제 스케줄러, userId={}, emitter={}", userId, emitter);
                } catch (IOException e) {
                  removeSSE(userId, emitter);
                  log.info("SSE 연결 해제, userId={}, emitter={}", userId, emitter);
                }
              });
        });
  }

  /**
   * @methodName : subscribe
   * @date : 2025. 6. 16. 17:16
   * @author : wongil
   * @Description: SSE 연결
   **/
  public SseEmitter subscribe(UUID userId, UUID lastEventId) {

    SseEmitter sseEmitter = generateEmitter(userId);

    retransmitEvents(userId, lastEventId, sseEmitter);

    return sseEmitter;
  }

  /**
   * @methodName : retransmitEvents
   * @date : 2025. 6. 17. 09:35
   * @author : wongil
   * @Description: last event id로 유실된 이벤트 재전송
   **/
  private void retransmitEvents(UUID userId, UUID lastEventId, SseEmitter sseEmitter) {
    if (lastEventId != null) {
      Deque<SseEvent> serverSentEvents = events.getOrDefault(userId, new ConcurrentLinkedDeque<>());

      boolean flag = false;
      for (SseEvent event : serverSentEvents) {
        if (flag) {
          try {
            sseEmitter.send(event);
          } catch (IOException e) {
            removeSSE(userId, sseEmitter);
          }
        } else if (lastEventId.equals(UUID.fromString(event.getId()))) {
          flag = true;
        }
      }
    }
  }

  public void broadcastEvent(User user, String name, String key, UUID value) {
    String eventId = String.valueOf(UUID.randomUUID());

    userConnections
        .forEach((userId, emitters) -> {
          emitters
              .forEach(emitter -> {
                try {
                  emitter.send(SseEmitter.event()
                      .id(eventId)
                      .name(name)
                      .data(Map.of(key, value))
                  );
                } catch (IOException e) {
                  removeSSE(userId, emitter);
                }
              });
        });
  }

  /**
   * @methodName : sendEvent
   * @date : 2025. 6. 17. 09:28
   * @author : wongil
   * @Description: 이벤트에 uuid 설정 및 전송
   **/
  public void sendEvent(UUID userId, String name, Object data) {
    log.info("=== SSE 이벤트 전송 시작 ===");
    String eventId = String.valueOf(UUID.randomUUID());

    Deque<SseEvent> serverSentEvents = events.computeIfAbsent(userId,
        id -> new ConcurrentLinkedDeque<>());
    serverSentEvents.addLast(new SseEvent(eventId, data));

    if (serverSentEvents.size() > MAX_QUEUE_SIZE) {
      serverSentEvents.removeFirst();
    }

    log.info("전체 연결된 사용자: {}", userConnections.keySet());

    Set<SseEmitter> emitters = userConnections.get(userId);
    if (emitters == null || emitters.isEmpty()) {
      log.warn("SSE 연결이 없습니다. userId={}", userId);
      return;
    }
    log.info("해당 사용자의 emitter 개수: {}", emitters.size());

    emitters
        .forEach(emitter -> {
          try {
            log.info("emitter 전송 시도 - userId: {}, emitter: {}", userId, emitter);
            SseEventBuilder ev = SseEmitter.event()
                .id(eventId)
                .name(name)
                .data(data, MediaType.APPLICATION_JSON);
            emitter.send(ev);
          } catch (IOException e) {
            removeSSE(userId, emitter);
            log.error("emitter 전송 실패 (IOException) - userId: {}, error: {}", userId,
                e.getMessage());
          } catch (Exception e) {
            log.error("emitter 전송 실패 (Exception) - userId: {}, error: {}", userId, e.getMessage(),
                e);
          }
        });

    log.info("=== SSE 이벤트 전송 완료 ===");
  }

  public void sendNotification(User user, UUID channelId, String name,
      NotificationType notificationType, String content, UUID targetId) {

    log.info("notification 저장 시작");
    List<ReadStatus> enabledNotification = readStatusRepository.findAllByChannel_IdAndNotificationEnabled(
        channelId, true);
    List<UUID> enableUser = enabledNotification.stream()
        .map(readStatus -> readStatus.getUser().getId())
        .filter(userId -> !userId.equals(user.getId()))
        .toList();

    enableUser.forEach(userId -> {

      User receiver = getUser(userId);
      User sender = getUser(user.getId());

      Notification notification = getNotification(receiver, sender, content,
          targetId, notificationType);
      notificationRepository.save(notification);
      log.info("notification 저장 완료");

      NotificationDto dto = toDto(channelId, notification, notificationType);
      sendEvent(userId, name, dto);
    });
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void sendChannelNotification(User user, UUID channelId, String name,
      NotificationType notificationType, String content, Map<String, UUID> data) {

    log.info("notification 저장 시작");
    List<ReadStatus> enabledNotification = readStatusRepository.findAllByChannel_IdAndNotificationEnabled(
        channelId, true);
    List<UUID> enableUser = enabledNotification.stream()
        .map(readStatus -> readStatus.getUser().getId())
        .toList();

    enableUser.forEach(userId -> {
      sendEvent(userId, name, data);
    });
  }

  private User getUser(UUID userId) {
    return userRepository.findById(userId)
        .orElseThrow(() -> new UserNotFoundException(Instant.now(), ErrorCode.USER_NOT_FOUND,
            Map.of(
                ErrorCode.USER_NOT_FOUND.getCode(),
                ErrorCode.USER_NOT_FOUND.getMessage()
            )));
  }

  private Notification getNotification(User receiver, User sender, String content, UUID targetId,
      NotificationType notificationType) {

    return Notification.builder()
        .receiver(receiver)
        .type(notificationType)
        .title(sender.getUsername())
        .content(content)
        .targetId(targetId)
        .build();
  }

  private NotificationDto toDto(UUID channelId,
      Notification notification, NotificationType notificationType) {

    return NotificationDto.builder()
        .id(notification.getId())
        .createdAt(notification.getCreatedAt())
        .receiverId(notification.getReceiver().getId())
        .title(notification.getTitle())
        .content(notification.getContent())
        .type(notificationType)
        .targetId(Optional.of(channelId))
        .build();
  }

  /**
   * @methodName : generateEmitter
   * @date : 2025. 6. 17. 09:10
   * @author : wongil
   * @Description: 사용자별 SSE Emitter
   **/
  private SseEmitter generateEmitter(UUID userId) {
    SseEmitter sseEmitter = new SseEmitter(Long.MAX_VALUE);
    userConnections.computeIfAbsent(userId,
            id -> ConcurrentHashMap.newKeySet())
        .add(sseEmitter);

    sseEmitter.onCompletion(() -> removeSSE(userId, sseEmitter));
    sseEmitter.onTimeout(() -> removeSSE(userId, sseEmitter));
    sseEmitter.onError((e) -> removeSSE(userId, sseEmitter));

    try {
      sseEmitter.send(SseEmitter.event()
          .name("connect")
          .data("connected")
      );
      log.info("SSE 구독 userId={}", userId);
    } catch (IOException e) {
      removeSSE(userId, sseEmitter);
      log.error("Sent Event IOException 발생: userId={}", userId);
    }

    return sseEmitter;
  }

  /**
   * @methodName : findAll
   * @date : 2025-06-04 오후 4:30
   * @author : wongil
   * @Description: 모든 알림 가져오기(자신만 알림 볼 수 있음)
   **/
//  @PreAuthorize("@authorizationChecker.isNotiUser(#userDto.id())")
  @Cacheable(cacheNames = "noti", key = "#userDto.id", sync = true)
  public List<NotificationDto> findAll(UserDto userDto) {

    List<Notification> notifications = notificationRepository.findAllByReceiver_IdOrderByCreatedAtDesc(
        userDto.id());
    if (notifications.isEmpty()) {
      return new ArrayList<>();
    }

    return notifications.stream()
        .map(noti -> new NotificationDto(noti.getId(), noti.getCreatedAt(),
            noti.getReceiver().getId(),
            noti.getTitle(), noti.getContent(), noti.getType(), Optional.of(noti.getTargetId())))
        .toList();
  }

  /**
   * @methodName : check
   * @date : 2025-06-04 오후 4:36
   * @author : wongil
   * @Description: 알림 삭제(확인)
   **/
  @CacheEvict(cacheNames = "noti", allEntries = true, beforeInvocation = true)
  @PreAuthorize("@authorizationChecker.isNotiUser(#userDto.id())")
  public void check(UserDto userDto, UUID notificationId) {

    if (notificationRepository.existsByReceiver_Id(userDto.id())) {
      notificationRepository.deleteById(notificationId);
    } else {
      throw new UserNotFoundException(Instant.now(), ErrorCode.USER_NOT_FOUND, Map.of(
          ErrorCode.USER_NOT_FOUND.getCode(),
          ErrorCode.USER_NOT_FOUND.getMessage()
      ));
    }
  }

  private void removeSSE(UUID userId, SseEmitter sseEmitter) {
    Set<SseEmitter> sseEmitters = userConnections.get(userId);
    if (sseEmitters != null) {
      sseEmitters.remove(sseEmitter);
      if (sseEmitters.isEmpty()) {
        userConnections.remove(userId);
      }
    }

    log.info("SSE 연결 해제, userId={}", userId);
  }
}
