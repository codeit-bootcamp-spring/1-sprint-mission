package com.sprint.mission.discodeit.listener;

import com.sprint.mission.discodeit.entity.Notification;
import com.sprint.mission.discodeit.entity.NotificationType;
import com.sprint.mission.discodeit.event.NotificationCreateEvent;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.repository.NotificationRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@RequiredArgsConstructor
@Component
public class NotificationEventListener {

  private final NotificationRepository notificationRepository;
  private final UserRepository userRepository;
  private final ReadStatusRepository readStatusRepository;

  private final CacheManager cacheManager;

  @Async
  @Retryable(
      value = { RuntimeException.class },
      maxAttempts = 3,
      backoff = @Backoff(delay = 2000)
  )
  @KafkaListener(topics = "notification-events", groupId = "discodeit-group")
  public void handleNotificationCreate(NotificationCreateEvent event) {
    log.info("알림 이벤트 수신: {}", event);

    switch (event.type()) {
      case NEW_MESSAGE -> handleNewMessage(event);
      case ROLE_CHANGED -> handleRoleChanged(event);
      case ASYNC_FAILED -> handleAsyncFailed(event);
      default -> throw new IllegalArgumentException("Unknown notification type: " + event.type());
    }
  }

  private void handleNewMessage(NotificationCreateEvent event) {
    List<UUID> users = readStatusRepository.findUserIdsByChannelIdAndNotificationEnabledTrue(event.targetId());
    for (UUID userId : users) {
      saveNotification(userId,
          event.title(),
          event.content(),
          event.type(),
          event.targetId());

      Cache cache = cacheManager.getCache("notifications");
      if (cache != null) {
        cache.evict(userId);
      }
    }
  }

  private void handleRoleChanged(NotificationCreateEvent event) {
    if (!userRepository.existsById(event.targetId())) {
      throw UserNotFoundException.withId(event.targetId());
    }
    saveNotification(event.targetId(),
        event.title(),
        event.content(),
        event.type(),
        event.targetId());

    Cache cache = cacheManager.getCache("notifications");
    if (cache != null) {
      cache.evict(event.targetId());
    }
  }

  private void handleAsyncFailed(NotificationCreateEvent event) {
    if (!userRepository.existsById(event.targetId())) {
      throw UserNotFoundException.withId(event.targetId());
    }
    saveNotification(event.targetId(),
        event.title(),
        event.content(),
        event.type(),
        null);

    Cache cache = cacheManager.getCache("notifications");
    if (cache != null) {
      cache.evict(event.targetId());
    }
  }

  private void saveNotification(UUID userId, String title, String content, NotificationType type, UUID targetId) {
    Notification notification = new Notification(userId, title, content, type, targetId);
    notificationRepository.save(notification);
    log.info("알림 저장 완료: {}", notification);
  }

}
