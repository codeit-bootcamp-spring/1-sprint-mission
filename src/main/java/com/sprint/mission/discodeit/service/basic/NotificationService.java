package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.NotificationDto;
import com.sprint.mission.discodeit.entity.Notification;
import com.sprint.mission.discodeit.entity.NotificationType;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.notification.NotificationNotFoundException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.NotificationMapper;
import com.sprint.mission.discodeit.repository.NotificationRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

  private final NotificationRepository notificationRepository;
  private final UserRepository userRepository;
  private final NotificationMapper notificationMapper;

  @CacheEvict(value = "userNotification", key = "#notificationDto.receiverId")
  public NotificationDto create(NotificationDto notificationDto) {
    Notification notification = Notification.builder()
        .title(notificationDto.getTitle())
        .content(notificationDto.getContent())
        .type(notificationDto.getType())
        .targetId(notificationDto.getTargetId())
        .receiverId(notificationDto.getReceiverId())
        .build();
    notification = notificationRepository.save(notification);

    return notificationMapper.toDto(notification);
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
