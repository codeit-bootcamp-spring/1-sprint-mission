package com.sprint.mission.discodeit.dto.data;

import com.sprint.mission.discodeit.entity.NotificationType;
import com.sprint.mission.discodeit.entity.User;
import java.time.Instant;
import java.util.UUID;

public record NotificationDto(
    UUID id,
    Instant createdAt,
    User receiver,
    String title,
    String content,
    NotificationType type,
    UUID targetId
) {

}
