package com.sprint.mission.discodeit.configure;

import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class NewMessageEvent extends NotificationEvent {

  public NewMessageEvent(UUID userId, UUID channelId) {
    super(userId, NotificationType.NEW_MESSAGE, channelId);
  }
}
