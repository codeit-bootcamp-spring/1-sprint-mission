package com.sprint.mission.discodeit.event;

import com.sprint.mission.discodeit.entity.NotificationType;
import java.util.UUID;
import lombok.Getter;

@Getter
public class NewMessageNotificationEvent extends NotificationEvent {

  public NewMessageNotificationEvent(UUID receiver, UUID channelId, String channelName,
      String message) {
    super(receiver, NotificationType.NEW_MESSAGE, channelId,
        channelName + "에 새 메시지가 있습니다.", message);
  }
}
