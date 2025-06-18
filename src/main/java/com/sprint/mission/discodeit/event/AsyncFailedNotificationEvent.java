package com.sprint.mission.discodeit.event;

import com.sprint.mission.discodeit.entity.NotificationType;
import java.util.UUID;


public class AsyncFailedNotificationEvent extends NotificationEvent {

  public AsyncFailedNotificationEvent(UUID receiverId, String errorMessage) {
    super(receiverId, NotificationType.ASYNC_FAILED, null,
        "작업 실패", "비동기 작업이 실패했습니다: " + errorMessage);
  }
}