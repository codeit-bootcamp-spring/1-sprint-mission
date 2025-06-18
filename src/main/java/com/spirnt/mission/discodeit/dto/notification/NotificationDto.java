package com.spirnt.mission.discodeit.dto.notification;

import com.spirnt.mission.discodeit.entity.NotificationType;
import java.time.Instant;
import java.util.UUID;

public record NotificationDto(
    UUID id,
    Instant createdAt,
    UUID receiverId,
    String title,
    String content,
    NotificationType type,
    UUID targetId
) {

}