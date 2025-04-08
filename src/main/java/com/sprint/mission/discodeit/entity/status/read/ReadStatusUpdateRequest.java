package com.sprint.mission.discodeit.entity.status.read;

import jakarta.validation.constraints.NotNull;
import java.time.Instant;

public record ReadStatusUpdateRequest(
    @NotNull
    Instant newLastReadAt
) {

}
