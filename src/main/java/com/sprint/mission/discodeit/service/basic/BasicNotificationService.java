package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.config.CacheNames;
import com.sprint.mission.discodeit.dto.NotificationDto;
import com.sprint.mission.discodeit.entity.Notification;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.event.NotificationEvent;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.notification.NotificationNotFoundException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.repository.NotificationRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.security.DiscodeitUserDetails;
import com.sprint.mission.discodeit.service.NotificationService;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BasicNotificationService implements NotificationService {

  private final CacheManager cacheManager;
  private final NotificationRepository notificationRepository;
  private final UserRepository userRepository;

  @CacheEvict(value = CacheNames.USER_NOTIFICATIONS, key = "#event.receiverId")
  @Override
  @Transactional
  public NotificationDto create(NotificationEvent event) {
    log.info("알림 생성 시작: 수신 사용자 ID = {}, 제목: {}", event.getReceiverId(), event.getTitle());

    User receiver = userRepository.findById(event.getReceiverId()).orElseThrow(
        () -> new UserNotFoundException(ErrorCode.USER_NOT_FOUND)
    );

    Notification notification = new Notification(
        receiver,
        event.getType(),
        event.getTitle(),
        event.getContent(),
        event.getTargetId()
    );

    notificationRepository.save(notification);
    log.info("알림 생성 완료: 수신 사용자 ID = {}, 제목: {}", receiver.getId(), event.getTitle());

    return NotificationDto.from(notification);
  }

  @Cacheable(value = CacheNames.USER_NOTIFICATIONS, key = "#userId")
  @PreAuthorize("#userId.equals(authentication.principal.userDto.id)")
  @Override
  @Transactional(readOnly = true)
  public List<NotificationDto> getNotifications(UUID userId) {

    log.debug("알림 조회 시작: 요청자 id = {}", userId);
    List<NotificationDto> notificationsDto = notificationRepository.findByReceiver_Id(userId)
        .stream()
        .map(notification ->
            new NotificationDto(notification.getId(),
                Instant.now(),
                userId,
                notification.getTitle(),
                notification.getContent(),
                notification.getType(),
                Optional.ofNullable(notification.getTargetId()))
        ).toList();

    log.debug("알림 조회 완료: 요청자 id = {}, 알림 개수 = {}", userId, notificationsDto.size());
    return notificationsDto;
  }

  @CacheEvict(value = CacheNames.USER_NOTIFICATIONS, key = "#event.receiverId")
  @Override
  @Transactional
  public void deleteNotification(UUID notificationId) {
    log.debug("알림 삭제 시작: 알림 id = {}", notificationId);

    Notification notification = notificationRepository.findById(notificationId).orElseThrow(
        () -> new NotificationNotFoundException(ErrorCode.NOTIFICATION_NOT_FOUND)
    );

    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    UUID currentUserId = ((DiscodeitUserDetails) auth.getPrincipal()).getUserDto().id();

    if (!notification.getReceiver().getId().equals(currentUserId)) {
      throw new AccessDeniedException("권한이 없습니다");
    }

    Objects.requireNonNull(cacheManager.getCache(CacheNames.USER_NOTIFICATIONS))
        .evict(notification.getReceiver().getId());

    notificationRepository.deleteById(notificationId);
    log.debug("알림 삭제 완료");
  }
}
