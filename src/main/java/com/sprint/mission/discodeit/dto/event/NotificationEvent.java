package com.sprint.mission.discodeit.dto.event;

import com.sprint.mission.discodeit.entity.NotificationType;
import java.util.UUID;

public record NotificationEvent(
    UUID receiverId,
    String title,
    String content,
    NotificationType type,
    UUID targetId
) {

}
