package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.data.NotificationDto;
import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.UUID;

public interface NotificationService {

  List<NotificationDto> getMyNotifications(UUID id);

  void deleteMyNotification(UUID notificationId, UUID id) throws AccessDeniedException;

}
