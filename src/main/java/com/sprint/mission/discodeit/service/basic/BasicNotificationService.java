package com.sprint.mission.discodeit.service.basic;

import static com.sprint.mission.discodeit.entity.Notification.newMessage;

import com.sprint.mission.discodeit.dto.data.NotificationDto;
import com.sprint.mission.discodeit.entity.Notification;
import com.sprint.mission.discodeit.entity.Notification.NotificationType;
import com.sprint.mission.discodeit.exception.notification.NotificationNotFoundException;
import com.sprint.mission.discodeit.mapper.NotificationMapper;
import com.sprint.mission.discodeit.repository.NotificationRepository;
import com.sprint.mission.discodeit.security.DiscodeitUserDetails;
import com.sprint.mission.discodeit.service.NotificationService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BasicNotificationService implements NotificationService {

  private final NotificationRepository notificationRepository;
  private final NotificationMapper notificationMapper;
  @Autowired
  private CacheManager cacheManager;

  public static UUID getCurrentUserId() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    DiscodeitUserDetails userDetails = (DiscodeitUserDetails) authentication.getPrincipal();
    return userDetails.getUserDto().id();
  }

  @Cacheable(cacheNames = "userNotifications", key = "#receiverId")
  @Override
  public List<NotificationDto> findAllByReceiverId(UUID receiverId) {
    List<Notification> notifications = notificationRepository.findAllByReceiverId(receiverId);

    return notifications.stream()
        .map(notificationMapper::toDto)
        .toList();
  }

  @Transactional
  @Override
  public NotificationDto createNotification(String content, NotificationType type,
      UUID targetId) {
    UUID receiverId = getCurrentUserId();

    log.debug("알림 생성 시작: content={}, type={}, targetId={}, receiverId={}", content, type, targetId,
        receiverId);

    Notification notification = switch (type) {
      case NEW_MESSAGE -> newMessage(receiverId, content, targetId);
      case ROLE_CHANGED -> Notification.roleChanged(receiverId, content, targetId);
      case ASYNC_FAILED -> Notification.asyncFailed(receiverId, content);
    };

    notificationRepository.save(notification);
    cacheManager.getCache("userNotifications").evict(receiverId);

    return null;
  }

  @Override
  public NotificationDto delete(UUID id) {
    Notification notification = notificationRepository.findById(id)
        .orElseThrow(() -> new NotificationNotFoundException(id));
    notificationRepository.deleteById(id);
    log.info("알림 삭제 완료: id={}", id);
    return notificationMapper.toDto(notification);
  }
}
