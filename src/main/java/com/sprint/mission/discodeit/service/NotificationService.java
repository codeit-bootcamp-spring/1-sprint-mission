package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.common.NotificationType;
import com.sprint.mission.discodeit.dto.notification.NotificationDto;
import java.util.List;
import java.util.UUID;

public interface NotificationService {

  NotificationDto create(UUID receiverId, UUID targetId, NotificationType type);

  List<NotificationDto> getNotifications(UUID userId);

  void deleteNotification(UUID notificationId, UUID userId);

}
