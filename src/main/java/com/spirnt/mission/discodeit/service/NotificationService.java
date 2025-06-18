package com.spirnt.mission.discodeit.service;

import com.spirnt.mission.discodeit.async.notification.NotificationCreateEvent;
import com.spirnt.mission.discodeit.dto.notification.NotificationDto;
import java.util.List;
import java.util.UUID;

public interface NotificationService {

    List<NotificationDto> create(NotificationCreateEvent event);

    List<NotificationDto> findAll(UUID userId);

    void delete(UUID notificationId);

}
