package com.sprint.mission.discodeit.event.notification;

import com.sprint.mission.discodeit.entity.NotificationType;
import java.util.UUID;

public record NotificationEvent(
    UUID userId,
    String title,
    String content,
    NotificationType type,
    UUID targetId
) {}

