package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.config.CacheName;
import com.sprint.mission.discodeit.dto.NotificationDto;
import com.sprint.mission.discodeit.entity.Notification;
import com.sprint.mission.discodeit.entity.Notification.Type;
import com.sprint.mission.discodeit.mapper.NotificationMapper;
import com.sprint.mission.discodeit.repository.NotificationRepository;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class NotificationService {

  private final NotificationRepository notificationRepository;
  private final NotificationMapper notificationMapper;
  private final SseService sseService;

  @Transactional
  @CacheEvict(cacheNames = CacheName.NOTIFICATIONS_BY_USER, key = "#receiverId")
  public NotificationDto create(
      UUID receiverId, String title, String content, Type type, UUID targetId
  ) {
    Notification notification = Notification.create(receiverId, title, content, type, targetId);
    notificationRepository.save(notification);
    log.info("알림 생성. id: {}", notification.getId());
    NotificationDto dto = notificationMapper.toDto(notification);

    TransactionSynchronizationManager.registerSynchronization(
        new TransactionSynchronization() {
          @Override
          public void afterCommit() {
            sseService.push(dto.receiverId(), "notifications", dto);
          }
        }
    );
    return dto;
  }

  @PreAuthorize("principal.user.id == #receiverId")
  @Cacheable(cacheNames = CacheName.NOTIFICATIONS_BY_USER, key = "#receiverId")
  public List<NotificationDto> findByReceiverId(UUID receiverId) {
    return notificationRepository.findByReceiverId(receiverId).stream()
        .map(notificationMapper::toDto)
        .toList();
  }

  @Transactional
  @PreAuthorize("principal.user.id == #receiverId")
  @CacheEvict(cacheNames = CacheName.NOTIFICATIONS_BY_USER, key = "#receiverId")
  public void delete(UUID notificationId, UUID receiverId) {
    notificationRepository.findById(notificationId).ifPresent(notificationRepository::delete);
  }
}
