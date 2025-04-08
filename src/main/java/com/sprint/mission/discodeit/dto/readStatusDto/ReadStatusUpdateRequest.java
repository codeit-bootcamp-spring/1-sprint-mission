package com.sprint.mission.discodeit.dto.readStatusDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;

public record ReadStatusUpdateRequest(
    @NotNull(message = "Last read time cannot null.")
    Instant newLastReadAt
) {

}
