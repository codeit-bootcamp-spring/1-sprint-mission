package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.NotificationDto;
import com.sprint.mission.discodeit.entity.Notification;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.notification.NotificationNotFoundException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.NotificationMapper;
import com.sprint.mission.discodeit.repository.EmitterRepository;
import com.sprint.mission.discodeit.repository.NotificationRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

  private final NotificationRepository notificationRepository;
  private final UserRepository userRepository;
  private final NotificationMapper notificationMapper;
  private final EmitterRepository emitterRepository;
  //
  private final Long DEFAULT_TIMEOUT = 1000 * 60L * 60; // 3_600_000 1시간 타임아웃

  // 사용자의 SSE 연결 등록
  public SseEmitter subscribe(String userId, String lastEventId) {
    // 1. Emitter ID 생성
    String emitterId = userId + "_" + System.currentTimeMillis();

    // 2. SseEmitter 객체 생성 및 저장
    SseEmitter sseEmitter = emitterRepository.save(emitterId, new SseEmitter(DEFAULT_TIMEOUT));

    /**
     * 3. 메모리 누수 방지를 위한 콜백 등록
     * onCompletion : SSE 연결이 정상적으로 완료
     * onTimeout : 타임아웃 발생 시
     * onError : 에러 발생 시
     * **/
    sseEmitter.onCompletion(() -> emitterRepository.deleteById(emitterId));
    sseEmitter.onTimeout(() -> emitterRepository.deleteById(emitterId));
    sseEmitter.onError((e) -> emitterRepository.deleteById(emitterId));

    // 4. 연결 직후 : 서버가 클라이언트에게 데이터 푸시
    sendToEmitter(sseEmitter, emitterId, "Connection Test", " SSE 연결 완료 : userId=" + userId + " ");

    /**
     * 5. 이벤트 유실 복구
     **/
    if (!lastEventId.isEmpty()) {
      Map<String, Object> eventCaches =
          emitterRepository.findAllEventCacheByUserId(userId);
      eventCaches.entrySet().stream()
          .filter(entry -> lastEventId.compareTo(entry.getKey()) < 0)
          .forEach(entry -> sendToEmitter(sseEmitter, entry.getKey(), "lost notification",
              entry.getValue()));
    }

    return sseEmitter;
  }

  // 다른 서비스 로직에서 이 메소드 호출
  public void send(User receiver, String eventName, Object data) {
    String userId = receiver.getId().toString();
    /**
     * 1. 이벤트 유실 복구를 위해 이벤트 캐시 저장
     **/
    String eventId = userId + "_" + System.currentTimeMillis();
    emitterRepository.saveEventCache(eventId, data);

    // 2. 해당 사용자에게 연결된 모든 Emitter 조회
    Map<String, SseEmitter> emitters = emitterRepository.findAllEmitterByUserId(userId);

    // 3. 각 Emitter에 이벤트 송신
    emitters.forEach(
        (emitterId, emitter) -> sendToEmitter(emitter, eventId, eventName, data)
    );
  }

  public void sendToAll(String eventName, Object data) {
    Map<String, SseEmitter> emitters = emitterRepository.findAllEmitter();

    emitters.forEach((emitterId, emitter) ->
    {
      String userId = emitterId.split("_")[0];
      String eventId = userId + "_" + System.currentTimeMillis();

      /**
       * 이벤트 유실 복구를 위해 이벤트 캐시 저장
       * **/
      emitterRepository.saveEventCache(eventId, data);

      sendToEmitter(emitter, eventId, eventName, data);
    });
  }

  /**
   * 서버 -> 클라이언트로 push
   **/
  private void sendToEmitter(SseEmitter emitter, String eventId, String eventName, Object data) {
    try {
      emitter.send(
          SseEmitter
              .event() // 서버가 클라이언트에게 데이터 푸시
              .id(eventId) // 클라이언트에게 보내는 이벤트 id
              .name(eventName)
              .data(data));
      log.info("SSE push 성공 : eventId={}, eventName={}, data={}", eventId, eventName, data);
    } catch (IOException e) {
      emitterRepository.deleteById(eventId);
    }
  }

  // -------------------------------------------------------------------------------------------

  @CacheEvict(value = "userNotification", key = "#notificationDto.receiverId")
  public NotificationDto create(NotificationDto notificationDto) {
    User receiver = userRepository.findById(notificationDto.getReceiverId())
        .orElseThrow(() -> new UserNotFoundException(Map.of("userId", notificationDto.getId())));
    Notification notification = Notification.builder()
        .title(notificationDto.getTitle())
        .content(notificationDto.getContent())
        .type(notificationDto.getType())
        .targetId(notificationDto.getTargetId())
        .receiverId(notificationDto.getReceiverId())
        .build();
    notification = notificationRepository.save(notification);
    NotificationDto notificationDtoSse = notificationMapper.toDto(notification);

    // 새로운 알림 이벤트 전송
    send(receiver, "notifications", notificationDtoSse);

    return notificationDtoSse;
  }

  @Cacheable(value = "userNotification", key = "#userId")
  public List<NotificationDto> find(UUID userId) {
    log.info("알림 조회 시작");

    User user = userRepository.findById(userId)
        .orElseThrow(() -> new UserNotFoundException(Map.of("username", userId)));

    List<Notification> notifications = notificationRepository.findByReceiverId(user.getId());

    return notifications.stream()
        .map(notificationMapper::toDto)
        .toList();
  }

  @CacheEvict(value = "userNotification", key = "#userId")
  public void delete(UUID id, UUID userId) {
    log.info("알림 삭제(확인) 시작");

    Notification notification = notificationRepository.findById(id)
        .orElseThrow(() -> new NotificationNotFoundException(Map.of("notificaitonId", id)));

    if (userId == notification.getReceiverId()) {
      notificationRepository.deleteById(id);
      log.info("알림 삭제(확인) 완료");
    } else {
      log.warn("유저 id와 알림 수신자의 id가 일치하지 않습니다. userId={}, receiverId={}", userId,
          notification.getReceiverId());
    }
  }
}
