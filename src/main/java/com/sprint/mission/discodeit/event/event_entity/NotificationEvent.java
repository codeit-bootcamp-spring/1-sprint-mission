package com.sprint.mission.discodeit.event.event_entity;

import com.sprint.mission.discodeit.entity.NotificationType;
import com.sprint.mission.discodeit.entity.User;
import java.util.List;
import java.util.UUID;

public record NotificationEvent(
    List<User> receivers,
    NotificationType type,
    UUID targetId,
    String title,
    String content
) {

}
