package com.sprint.mission.discodeit.dto;

import com.sprint.mission.discodeit.configure.NotificationType;
import com.sprint.mission.discodeit.entity.Notification;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

public record NotificationDto(
    UUID id,
    Instant createdAt,
    String title,
    String content,
    NotificationType type,
    UUID targetId

) {

  public static NotificationDto from(Notification notification) {
    return new NotificationDto(
        notification.getId(),
        notification.getCreatedAt(),
        notification.getTitle(),
        notification.getMessage(),
        notification.getType(),
        notification.getTargetId()

    );
  }
}
