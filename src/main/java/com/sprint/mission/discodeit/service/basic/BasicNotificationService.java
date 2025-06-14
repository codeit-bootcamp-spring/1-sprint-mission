package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.data.NotificationDto;
import com.sprint.mission.discodeit.entity.Notification;
import com.sprint.mission.discodeit.entity.NotificationType;
import com.sprint.mission.discodeit.event.NotificationCreateEvent;
import com.sprint.mission.discodeit.mapper.NotificationMapper;
import com.sprint.mission.discodeit.repository.NotificationRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.NotificationService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
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

  @Override
  public void create(NotificationType type, UUID targetId, String title, String content) {
    eventPublisher.publishEvent(new NotificationCreateEvent(type, targetId, title, content));
  }

  @Override
  public NotificationDto find(UUID notificationId) {
    Notification notification = notificationRepository.findById(notificationId)
        .orElseThrow(() -> new IllegalArgumentException());

    return notificationMapper.toDto(notification);
  }

  @Override
  public List<NotificationDto> findAllByUserId(UUID userId) {
    List<NotificationDto> notifications = notificationRepository.findAllByReceiverId(userId).stream()
        .map(notificationMapper::toDto)
        .toList();

    return notifications;
  }

  @PreAuthorize("principal.userDto.id == @basicNotificationService.find(#notificationId).receiverId()")
  @Override
  @Transactional
  public void delete(UUID notificationId) {
    notificationRepository.deleteById(notificationId);
  }
}
