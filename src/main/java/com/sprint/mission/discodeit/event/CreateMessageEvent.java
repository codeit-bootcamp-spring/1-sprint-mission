package com.sprint.mission.discodeit.event;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.NotificationType;

public record CreateMessageEvent(
    NotificationType notificationType,
    Channel channel,
    Message message
) {

}
