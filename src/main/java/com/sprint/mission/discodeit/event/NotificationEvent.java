package com.sprint.mission.discodeit.event;

import com.sprint.mission.discodeit.entity.NotificationType;
import java.util.UUID;
import lombok.Getter;

@Getter
public class NotificationEvent {
  private final UUID receiverId;
  private final String title;
  private final String content;
  private final NotificationType type;
  private final UUID targetId;

  public NotificationEvent(UUID receiverId, String title, String content,
      NotificationType type, UUID targetId) {
    this.receiverId = receiverId;
    this.title = title;
    this.content = content;
    this.type = type;
    this.targetId = targetId;
  }
}