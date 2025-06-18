package com.sprint.mission.discodeit.event;

import com.sprint.mission.discodeit.entity.NotificationType;
import java.util.UUID;
import lombok.Getter;

@Getter
public abstract class NotificationEvent {

  private final UUID receiverId;
  private final NotificationType type;
  private final UUID targetId;
  private final String title;
  private final String content;

  protected NotificationEvent(UUID receiverId, NotificationType type, UUID targetId, String title,
      String content) {
    this.receiverId = receiverId;
    this.type = type;
    this.targetId = targetId;
    this.title = title;
    this.content = content;
  }
}