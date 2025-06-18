package com.sprint.mission.discodeit.event;

import com.sprint.mission.discodeit.entity.NotificationType;

public record AsyncTaskFailureEvent(
    NotificationType notificationType,
    String taskName,
    String username
) {

}
