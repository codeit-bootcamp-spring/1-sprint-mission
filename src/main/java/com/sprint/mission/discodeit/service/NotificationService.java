package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.data.NotificationDto;
import com.sprint.mission.discodeit.entity.NotificationType;
import java.util.List;
import java.util.UUID;

public interface NotificationService {
  void create(NotificationType type, UUID targetId, String title, String content);
  NotificationDto find(UUID notificationId);
  List<NotificationDto> findAllByUserId(UUID userId);
  void delete(UUID notificationId);
}
