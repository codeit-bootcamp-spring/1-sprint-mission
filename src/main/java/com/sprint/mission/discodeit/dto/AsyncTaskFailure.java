package com.sprint.mission.discodeit.dto;

public record AsyncTaskFailure(
    String taskName,
    String requestId,
    String failureReason
) {

}
