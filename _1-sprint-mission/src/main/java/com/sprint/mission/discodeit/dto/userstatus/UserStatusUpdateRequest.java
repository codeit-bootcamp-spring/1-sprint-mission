package com.sprint.mission.discodeit.dto.userstatus;

import jakarta.validation.constraints.NotNull;
import java.time.Instant;

public record UserStatusUpdateRequest(
    @NotNull
    Instant newLastActiveAt
) {

}
