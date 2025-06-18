package com.sprint.mission.discodeit.dto.event;

import java.util.UUID;

public record AsyncTaskFailedEvent(
    UUID userId,
    String taskName,
    String requestId,
    String failureReason
) {

}
