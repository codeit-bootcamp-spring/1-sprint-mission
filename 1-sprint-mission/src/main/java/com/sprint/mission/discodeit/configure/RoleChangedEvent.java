package com.sprint.mission.discodeit.configure;

import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class RoleChangedEvent extends NotificationEvent {

  public RoleChangedEvent(UUID userId) {
    super(userId, NotificationType.ROLE_CHANGED, userId);
  }
}
