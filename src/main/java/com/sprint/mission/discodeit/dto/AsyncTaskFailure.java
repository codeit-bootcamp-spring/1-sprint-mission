package com.sprint.mission.discodeit.dto;

import lombok.Builder;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Builder
public class AsyncTaskFailure {

  private final String taskName;
  private final String requestId;
  private final String failureReason;

}
