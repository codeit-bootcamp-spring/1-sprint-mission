package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.data.NotificationDto;
import com.sprint.mission.discodeit.exception.notification.NotificationNotFoundException;
import com.sprint.mission.discodeit.mapper.NotificationMapper;
import com.sprint.mission.discodeit.repository.NotificationRepository;
import com.sprint.mission.discodeit.service.NotificationService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BasicNotificationService implements NotificationService {

  private final NotificationRepository notificationRepository;
  private final NotificationMapper notificationMapper;

  @Override
  @Transactional(readOnly = true)
  @PreAuthorize("principal.userDto.id == #userId")
  @Cacheable(cacheNames = "notificationList", key = "#userId")
  public List<NotificationDto> findAll(UUID userId) {
    log.debug("알림 조회 시작: userId={}", userId);
    return notificationRepository.findAllByReceiverId(userId).stream()
        .map(notificationMapper::toDto)
        .toList();
  }

  @Override
  @PreAuthorize("hasRole('ADMIN') or principal.userDto.id == @basicNotificationService.find(#notificationId).receiverId()")
  @Transactional
  public void deleteById(UUID notificationId) {
    log.debug("알림 삭제 시작: id={}", notificationId);
    if (!notificationRepository.existsById(notificationId)) {
      throw NotificationNotFoundException.withId(notificationId);
    }
    notificationRepository.deleteById(notificationId);
    log.info("알림 삭제 완료: id={}", notificationId);
  }

  @Override
  @Transactional(readOnly = true)
  public NotificationDto find(UUID notificationId) {
    return notificationRepository.findById(notificationId)
        .map(notificationMapper::toDto)
        .orElseThrow(() -> NotificationNotFoundException.withId(notificationId));
  }

}
