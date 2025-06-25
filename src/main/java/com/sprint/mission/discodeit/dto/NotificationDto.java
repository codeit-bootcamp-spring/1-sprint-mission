package com.sprint.mission.discodeit.dto;

import com.sprint.mission.discodeit.entity.Notification.Type;
import java.time.Instant;
import java.util.UUID;

public record NotificationDto(
    UUID id,
    Instant createdAt,
    UUID receiverId,
    String title,
    String content,
    Type type,
    UUID targetId
) {

}
