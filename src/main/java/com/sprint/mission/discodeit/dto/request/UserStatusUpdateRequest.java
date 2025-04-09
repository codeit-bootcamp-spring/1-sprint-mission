package com.sprint.mission.discodeit.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import java.time.Instant;

public record UserStatusUpdateRequest(
    @NotNull
    @PastOrPresent(message = "The new last active time must be in the past or the present")
    Instant newLastActiveAt
) {

}
