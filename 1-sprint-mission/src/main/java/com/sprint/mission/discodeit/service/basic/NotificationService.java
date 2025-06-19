package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.configure.NotificationEvent;
import com.sprint.mission.discodeit.configure.NotificationType;
import com.sprint.mission.discodeit.dto.NotificationDto;
import com.sprint.mission.discodeit.dto.request.NotificationCreatedEvent;
import com.sprint.mission.discodeit.entity.Notification;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.NotificationRepository;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

  private final NotificationRepository notificationRepository;
  private final ApplicationEventPublisher eventPublisher;

  @Cacheable(value = "userNotifications", key = "#userId")
  @Transactional
  public List<NotificationDto> getUserNotifications(UUID userId) {
    return notificationRepository.findByReceiverId(userId).stream()
        .map(NotificationDto::from)
        .toList();
  }

  @Transactional
  public void deleteNotification(UUID notificationId, UUID userId) {
    Notification notification = notificationRepository
        .findByIdAndReceiverId(notificationId, userId)
        .orElseThrow(() -> new AccessDeniedException("알림을 삭제할 권한이 없습니다."));
    notificationRepository.delete(notification);
  }

  private final BasicReadStatusService readStatusService;
  private final BasicUserService userService;

  public boolean isNotificationEnabled(UUID userId, UUID channelId) {
    return readStatusService.findByUserIdAndChannelId(userId, channelId).isNotificationEnabled();
  }

  @Transactional
  public void createNotification(UUID userId, NotificationType type, UUID targetId) {
    User receiver = userService.findById(userId);

    Notification notification = new Notification(receiver, type.name(), targetId,
        NotificationType.NEW_MESSAGE, "새로운 알림");
    notificationRepository.save(notification);
    eventPublisher.publishEvent(new NotificationCreatedEvent(userId));
  }

  public void recordFailure(NotificationEvent event, Exception e) {
    log.error("Notification event 처리 실패: {} for user {}", event.getType(), event.getUserId(), e);
  }
  
  private final CacheManager cacheManager;

  @EventListener
  public void onNotificationCreated(NotificationCreatedEvent event) {
    cacheManager.getCache("userNotifications").evict(event.receiverId());
  }
}
