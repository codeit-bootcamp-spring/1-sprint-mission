package com.sprint.mission.discodeit.event;

import java.util.UUID;

public record AuthenticatedAsyncTaskFailedEvent(
    AsyncTaskFailedEvent asyncTaskFailedEvent,
    UUID authenticatedUserId
) {

}