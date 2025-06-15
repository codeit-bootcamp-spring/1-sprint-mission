package com.sprint.mission.discodeit.dto.data;

import com.sprint.mission.discodeit.entity.NotificationType;
import java.util.UUID;

public record NotificationEvent(
    UUID receiverId,
    NotificationType type,
    UUID targetId,
    String message
) {}
