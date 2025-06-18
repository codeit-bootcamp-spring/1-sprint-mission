package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.data.NotificationDto;
import com.sprint.mission.discodeit.entity.Notification.NotificationType;
import java.util.List;
import java.util.UUID;

public interface NotificationService {

  List<NotificationDto> findAllByReceiverId(UUID receiverId);

  NotificationDto createNotification(
      String content,
      NotificationType type,
      UUID targetId);

  NotificationDto delete(UUID id);


}
