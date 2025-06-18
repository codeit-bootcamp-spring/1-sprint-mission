package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.common.NotificationType;
import com.sprint.mission.discodeit.dto.notification.NotificationDto;
import com.sprint.mission.discodeit.entity.Notification;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.NotificationMapper;
import com.sprint.mission.discodeit.repository.NotificationRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.NotificationService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BasicNotificationService implements NotificationService {

  private final UserRepository userRepository;
  private final NotificationRepository notificationRepository;
  private final NotificationMapper notificationMapper;

  @Override
  @CacheEvict(value = "userNotifications", key = "#receiverId")
  @Transactional(propagation = Propagation.REQUIRED)
  public NotificationDto create(UUID receiverId, UUID targetId, NotificationType type) {
    User receiver = userRepository.findById(receiverId)
        .orElseThrow(() -> new UserNotFoundException(receiverId));

    Notification notification = Notification.builder()
        .receiverId(receiver.getId())
        .notificationType(type)
        .targetId(targetId)
        .title(type.getTitle())
        .content(type.getMessage())
        .build();

    log.info("✨ 알림 생성: {}", notification);
    Notification saved = notificationRepository.save(notification);

    return notificationMapper.toResponseDto(saved);
  }

  @Override
  @Cacheable(value = "userNotifications", key = "#userId", unless = "#result.isEmpty()")
  public List<NotificationDto> getNotifications(UUID userId) {
    log.info("알림 불러오기 - 사용자 아이디: {}", userId);

    return notificationRepository.getAllByReceiverId(userId)
        .stream()
        .map(notificationMapper::toResponseDto)
        .toList();
  }

  @Override
  @CacheEvict(value = "userNotifications", key = "#userId")
  public void deleteNotification(UUID notificationId, UUID userId) {
    Notification notification = notificationRepository.findById(notificationId).orElseThrow();
    notificationRepository.delete(notification);
  }

}
