package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.event.MultipleNotificationCreatedEvent;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationEventPublisher;

import com.sprint.mission.discodeit.dto.data.NotificationDto;
import com.sprint.mission.discodeit.entity.Notification;
import com.sprint.mission.discodeit.entity.NotificationType;
import com.sprint.mission.discodeit.exception.notification.NotificationNotFoundException;
import com.sprint.mission.discodeit.mapper.NotificationMapper;
import com.sprint.mission.discodeit.repository.NotificationRepository;
import com.sprint.mission.discodeit.service.NotificationService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class BasicNotificationService implements NotificationService {

  private final NotificationRepository notificationRepository;
  private final NotificationMapper notificationMapper;
  private final ApplicationEventPublisher eventPublisher;

  @PreAuthorize("principal.userDto.id == #receiverId")
  @Cacheable(value = "notificationsByUser", key = "#receiverId", unless = "#result.isEmpty()")
  @Override
  public List<NotificationDto> findAllByReceiverId(UUID receiverId) {
    log.debug("알림 목록 조회 시작: receiverId={}", receiverId);
    List<NotificationDto> notifications = notificationRepository.findAllByReceiverIdOrderByCreatedAtDesc(
            receiverId)
        .stream()
        .map(notificationMapper::toDto)
        .toList();
    log.info("알림 목록 조회 완료: receiverId={}, 조회된 항목 수={}", receiverId, notifications.size());
    return notifications;
  }

  @PreAuthorize("principal.userDto.id == #receiverId")
  @Transactional
  @CacheEvict(value = "notificationsByUser", key = "#receiverId")
  @Override
  public void delete(UUID notificationId, UUID receiverId) {
    log.debug("알림 삭제 시작: id={}, receiverId={}", notificationId, receiverId);
    try {
      notificationRepository.deleteByIdAndReceiverId(notificationId, receiverId);
      log.info("알림 삭제 완료: id={}, receiverId={}", notificationId, receiverId);
    } catch (Exception e) {
      log.error("알림 삭제 실패: id={}, receiverId={}", notificationId, receiverId, e);
      throw NotificationNotFoundException.withId(notificationId);
    }
  }

  @Transactional
  @CacheEvict(value = "notificationsByUser", key = "#receiverId")
  @Override
  public void create(UUID receiverId, String title, String content,
      NotificationType notificationType, UUID targetId) {
    log.debug("새 알림 생성 시작: receiverId={}, channelId={}", receiverId);

    Notification notification = new Notification(
        receiverId,
        title,
        content,
        notificationType,
        targetId
    );
    notificationRepository.save(notification);
    log.info("새 알림 생성 완료: id={}, receiverId={}, targetId={}",
        notification.getId(), receiverId, targetId);
  }

  @Transactional
  @Override
  public void createAll(Set<UUID> receiverIds, String title, String content,
      NotificationType notificationType, UUID targetId) {
    log.debug("새 알림 생성 시작: receiverIds={}, targetId={}", receiverIds, targetId);
    List<Notification> notifications = receiverIds.stream()
        .map(receiverId -> new Notification(
            receiverId,
            title,
            content,
            notificationType,
            targetId
        )).toList();
    notificationRepository.saveAll(notifications);

    // 이벤트 발행
    eventPublisher.publishEvent(new MultipleNotificationCreatedEvent(receiverIds));

    log.info("새 알림 생성 완료: receiverIds={}, targetId={}",
        receiverIds, targetId);
  }
} 