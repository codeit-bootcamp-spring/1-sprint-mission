package com.sprint.mission.discodeit.event;

import com.sprint.mission.discodeit.entity.NotificationType;
import com.sprint.mission.discodeit.entity.User;

public record UpdateUserRoleEvent(
    NotificationType notificationType,
    User user
) {

}
