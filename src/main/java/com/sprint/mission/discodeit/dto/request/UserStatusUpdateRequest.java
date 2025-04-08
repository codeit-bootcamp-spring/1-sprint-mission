package com.sprint.mission.discodeit.dto.request;

import jakarta.validation.constraints.NotNull;

import java.time.Instant;

public record UserStatusUpdateRequest(
        @NotNull(message = "Last active time cannot null.")
        Instant newLastActiveAt
) {

}
