package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.data.NotificationDto;
import com.sprint.mission.discodeit.entity.NotificationType;
import com.sprint.mission.discodeit.event.NotificationEvent;
import com.sprint.mission.discodeit.exception.Notification.NotificationNotFoundException;
import com.sprint.mission.discodeit.mapper.NotificationMapper;
import com.sprint.mission.discodeit.repository.NotificationRepository;
import com.sprint.mission.discodeit.service.NotificationService;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.access.AccessDeniedException;

@Service
@RequiredArgsConstructor
public class BasicNotificationService implements NotificationService {

  private final NotificationMapper notificationMapper;
  private final NotificationRepository notificationRepository;
  private final ApplicationEventPublisher eventPublisher;

  @Override
  @Cacheable(value = "notifications", key = "#userId")
  @Transactional(readOnly = true)
  public List<NotificationDto> getNotificationsByUserId(UUID userId) {
    return notificationRepository.findAllByReceiver_IdOrderByCreatedAtDesc(userId)
        .stream()
        .map(notificationMapper::toDto)
        .collect(Collectors.toList());
  }

  @Override
  @Transactional
  @CacheEvict(value = "notifications", key = "#userId")
  public void deleteNotification(UUID notificationId, UUID userId) {
    var notification = notificationRepository.findById(notificationId)
        .orElseThrow(NotificationNotFoundException::new);

    if (!notification.getReceiver().getId().equals(userId)) {
      throw new AccessDeniedException("본인의 알림만 삭제할 수 있습니다.");
    }

    notificationRepository.delete(notification);
  }

  @Override
  @CacheEvict(value = "notifications", key = "#receiverId")
  public void publishNotificationEvent(UUID receiverId, String title, String content,
      NotificationType type, UUID targetId) {
    eventPublisher.publishEvent(new NotificationEvent(receiverId, title, content, type, targetId));
  }
}