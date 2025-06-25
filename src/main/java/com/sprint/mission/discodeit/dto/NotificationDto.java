package com.sprint.mission.discodeit.dto;

import com.sprint.mission.discodeit.entity.Notification;
import com.sprint.mission.discodeit.entity.NotificationType;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;


public record NotificationDto(
    UUID id,
    Instant createdAt,
    UUID receiverId,
    String title,
    String content,
    NotificationType type,
    Optional<UUID> targetId
) {

  public static NotificationDto from(Notification notification) {
    return new NotificationDto(
        notification.getId(),
        notification.getCreatedAt(),
        notification.getReceiver().getId(),
        notification.getTitle(),
        notification.getContent(),
        notification.getType(),
        Optional.ofNullable(notification.getTargetId())
    );
  }

}
