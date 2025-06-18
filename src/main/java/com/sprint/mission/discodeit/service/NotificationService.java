package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.data.NotificationDto;
import com.sprint.mission.discodeit.entity.NotificationType;
import java.util.List;
import java.util.UUID;

public interface NotificationService {
  List<NotificationDto> getNotificationsByUserId(UUID userId);
  void deleteNotification(UUID notificationId, UUID userId);
  void publishNotificationEvent(UUID receiverId, String title, String content,
      NotificationType type, UUID targetId);
}