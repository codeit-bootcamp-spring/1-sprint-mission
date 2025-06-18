package com.sprint.mission.discodeit.dto.data;

import com.sprint.mission.discodeit.entity.Notification;
import java.time.Instant;
import java.util.UUID;

public record NotificationDto(
    UUID id,
    Instant createdAt,
    UUID receiverId,
    String title,
    String content,
    Notification.NotificationType type,
    UUID targetId
) {

}
