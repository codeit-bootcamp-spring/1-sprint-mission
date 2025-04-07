package com.sprint.mission.discodeit.dto.request;

import jakarta.validation.constraints.NotNull;

import java.time.Instant;

public record ReadStatusUpdateRequest(
        @NotNull(message = "Last read time cannot null.")
        Instant newLastReadAt
) {

}
