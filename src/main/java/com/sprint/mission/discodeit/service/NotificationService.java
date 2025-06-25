package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.response.NotificationResponse;
import com.sprint.mission.discodeit.event.NotificationEvent;
import java.util.List;
import java.util.UUID;

public interface NotificationService {

    List<NotificationResponse> getMyNotifications(UUID receiverId);

    void readNotification(UUID id, UUID receiverId);

    void createNotification(NotificationEvent notificationEvent);
}
