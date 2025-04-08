package com.sprint.mission.discodeit.dto.userStatusDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import java.util.UUID;

public record UserStatusCreateRequest(
    @NotNull(message = "User id cannot null.")
    UUID userId,

    @NotNull(message = "Last active time cannot null.")
    Instant lastActiveAt
) {

}
