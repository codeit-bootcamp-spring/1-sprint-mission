package com.sprint.mission.discodeit.event;

import java.time.Instant;

import com.sprint.mission.discodeit.entity.AsyncTaskFailure;

public record AsyncTaskFailedEvent(
    Instant createdAt,
    AsyncTaskFailure asyncTaskFailure
) {
  public AsyncTaskFailedEvent(AsyncTaskFailure asyncTaskFailure) {
    this(Instant.now(), asyncTaskFailure);
  }
} 