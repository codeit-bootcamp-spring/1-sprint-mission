package com.spirnt.mission.discodeit.async.notification;

import com.spirnt.mission.discodeit.entity.NotificationType;
import com.spirnt.mission.discodeit.entity.User;
import java.util.List;
import java.util.UUID;

public record NotificationCreateEvent(
    List<User> receivers,
    String title,
    String content,
    NotificationType type,
    UUID targetId
) {

}
