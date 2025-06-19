package com.sprint.mission.discodeit.configure;

import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class AsyncFailedEvent extends NotificationEvent {

  public AsyncFailedEvent(UUID userId) {
    super(userId, NotificationType.ASYNC_FAILED, null);
  }
}
