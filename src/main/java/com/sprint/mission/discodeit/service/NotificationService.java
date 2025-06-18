package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.NotificationDto;
import com.sprint.mission.discodeit.event.NotificationEvent;
import java.util.List;
import java.util.UUID;

public interface NotificationService {

  void create(NotificationEvent notificationEvent);

  List<NotificationDto> getNotifications(UUID userId);

  void deleteNotification(UUID id);
}
