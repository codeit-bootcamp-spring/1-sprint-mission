package com.sprint.mission.discodeit.dto.data;

public record AsyncTaskFailure(
    String taskName,
    String requestId,
    String failureReason
) {

}
