package com.sprint.mission.discodeit.event;

import com.sprint.mission.discodeit.entity.NotificationType;
import java.util.UUID;

public record NotificationCreateEvent(
    NotificationType type,
    UUID targetId,
    String title,
    String content
) {

}
