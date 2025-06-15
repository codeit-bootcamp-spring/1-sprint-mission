package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.data.NotificationDto;
import com.sprint.mission.discodeit.entity.Notification;
import com.sprint.mission.discodeit.entity.NotificationType;
import com.sprint.mission.discodeit.event.NotificationCreateEvent;
import com.sprint.mission.discodeit.mapper.NotificationMapper;
import com.sprint.mission.discodeit.repository.NotificationRepository;
import com.sprint.mission.discodeit.service.NotificationService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BasicNotificationService implements NotificationService {

  private final NotificationRepository notificationRepository;
  private final NotificationMapper notificationMapper;

  private final ApplicationEventPublisher eventPublisher;
  private final KafkaTemplate<String, NotificationCreateEvent> kafkaTemplate;

  @Override
  public void create(NotificationType type, UUID targetId, String title, String content) {
    NotificationCreateEvent event = new NotificationCreateEvent(type, targetId, title, content);

    eventPublisher.publishEvent(event);
    kafkaTemplate.send("notification-events", event);
  }

  @Transactional(readOnly = true)
  @Override
  public NotificationDto find(UUID notificationId) {
    Notification notification = notificationRepository.findById(notificationId)
        .orElseThrow(() -> new IllegalArgumentException());

    return notificationMapper.toDto(notification);
  }

  @Transactional(readOnly = true)
  @Cacheable(value = "notifications", key = "#userId")
  @Override
  public List<NotificationDto> findAllByUserId(UUID userId) {
    List<NotificationDto> notifications = notificationRepository.findAllByReceiverId(userId).stream()
        .map(notificationMapper::toDto)
        .toList();

    return notifications;
  }

  @PreAuthorize("principal.userDto.id == @basicNotificationService.find(#notificationId).receiverId()")
  @CacheEvict(value = "notifications", key = "#userId")
  @Override
  @Transactional
  public void delete(UUID notificationId) {
    notificationRepository.deleteById(notificationId);
  }
}
