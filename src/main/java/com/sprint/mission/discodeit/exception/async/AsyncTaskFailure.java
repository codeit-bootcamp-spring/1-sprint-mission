package com.sprint.mission.discodeit.exception.async;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class AsyncTaskFailure {

  private String taskName;
  private String requestId;
  private String failureReason;

}
