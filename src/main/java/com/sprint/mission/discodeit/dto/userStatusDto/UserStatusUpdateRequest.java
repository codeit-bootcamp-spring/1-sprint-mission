package com.sprint.mission.discodeit.dto.userStatusDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;

public record UserStatusUpdateRequest(
    @NotNull(message = "Last active time cannot null.")
    Instant newLastActiveAt
) {

}
