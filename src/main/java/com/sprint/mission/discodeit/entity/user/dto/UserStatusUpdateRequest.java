package com.sprint.mission.discodeit.entity.user.dto;

import jakarta.validation.constraints.NotNull;
import java.time.Instant;

public record UserStatusUpdateRequest(
    @NotNull
    Instant newLastActiveAt
) {

}
