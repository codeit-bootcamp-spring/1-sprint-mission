package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.data.NotificationDto;
import com.sprint.mission.discodeit.mapper.NotificationMapper;
import com.sprint.mission.discodeit.repository.NotificationRepository;
import java.util.List;
import java.util.UUID;
import javax.management.Notification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NotificationService {

  private final NotificationRepository notificationRepository;
  private final NotificationMapper notificationMapper;

  public List<NotificationDto> getMyNotifications(UUID userId) {
    return notificationRepository.findByReceiverIdOrderByCreatedAtDesc(userId)
        .stream()
        .map(notificationMapper::toDto)
        .toList();
  }

  @Transactional
  public void deleteMyNotification(UUID userId, UUID notificationId) {
    Notification notification = notificationRepository.findByIdAndReceiverId(notificationId, userId)
        .orElseThrow(NotFoundException::new);
    notificationRepository.delete(notification);
  }
}

