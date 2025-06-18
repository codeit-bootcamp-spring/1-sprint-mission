package com.sprint.mission.discodeit.ASync;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AsyncTaskFailure {
  private final String taskName;
  private final String requestId;
  private final String failureReason;

  public AsyncTaskFailure(String taskName, String requestId, String failureReason) {
    this.taskName = taskName;
    this.requestId = requestId;
    this.failureReason = failureReason;
  }
}