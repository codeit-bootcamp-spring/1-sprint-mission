package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.data.NotificationDto;
import java.util.List;
import java.util.UUID;

public interface NotificationService {

  List<NotificationDto> findAll(UUID userId);

  void deleteById(UUID notificationId);

  NotificationDto find(UUID notificationId);
}
