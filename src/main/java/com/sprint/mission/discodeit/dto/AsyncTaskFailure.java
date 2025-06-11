package com.sprint.mission.discodeit.dto;

public record AsyncTaskFailure(
    String taskName,
    String requestId,
    String failureReason
) {

  @Override
  public String toString() {
    return "테스크 = " + taskName
        + " / MDC = " + requestId
        + " / 실패 이유 = " + failureReason;
  }

}
