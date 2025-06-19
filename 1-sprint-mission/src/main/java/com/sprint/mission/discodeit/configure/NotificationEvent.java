package com.sprint.mission.discodeit.configure;

import java.util.UUID;
import lombok.Getter;
import org.springframework.stereotype.Component;

@Getter
@Component
public abstract class NotificationEvent {

  private final UUID userId;
  private final NotificationType type;
  private final UUID targetId;

  protected NotificationEvent(UUID userId, NotificationType type, UUID targetId) {
    this.userId = userId;
    this.type = type;
    this.targetId = targetId;
  }
}


