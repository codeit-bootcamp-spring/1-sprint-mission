package com.sprint.mission.discodeit.dto.response;

import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import java.util.UUID;

public record UserStatusDto(
    UUID id,
    UUID userId,

    @NotNull
    Instant lastActiveAt
) {

}
