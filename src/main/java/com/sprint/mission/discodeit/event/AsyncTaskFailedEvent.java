package com.sprint.mission.discodeit.event;

import com.sprint.mission.discodeit.dto.AsyncTaskFailure;
import com.sprint.mission.discodeit.entity.Notification.Type;

public record AsyncTaskFailedEvent(
    Type type,
    AsyncTaskFailure asyncTaskFailure
) {

  public static AsyncTaskFailedEvent of(AsyncTaskFailure asyncTaskFailure) {
    return new AsyncTaskFailedEvent(Type.ASYNC_FAILED, asyncTaskFailure);
  }

}
