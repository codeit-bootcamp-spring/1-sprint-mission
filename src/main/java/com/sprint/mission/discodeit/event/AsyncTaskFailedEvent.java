package com.sprint.mission.discodeit.event;

import com.sprint.mission.discodeit.entity.AsyncTaskFailure;
import java.time.Instant;

public record AsyncTaskFailedEvent(
    Instant createdAt,
    AsyncTaskFailure asyncTaskFailure
) {

  public AsyncTaskFailedEvent(AsyncTaskFailure asyncTaskFailure) {
    this(Instant.now(), asyncTaskFailure);
  }
} 