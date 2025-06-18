package com.sprint.mission.discodeit.event;

import com.sprint.mission.discodeit.dto.AsyncTaskFailure;
import com.sprint.mission.discodeit.entity.Notification.Type;

public record AsyncFailedEvent(
    Type type,
    AsyncTaskFailure asyncTaskFailure
) {

  public static AsyncFailedEvent of(AsyncTaskFailure asyncTaskFailure) {
    return new AsyncFailedEvent(Type.ASYNC_FAILED, asyncTaskFailure);
  }

}
