package com.sprint.mission.discodeit.dto.readStatusDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import java.util.UUID;

public record ReadStatusCreateRequest(
    @NotNull(message = "User id cannot null.")
    UUID userId,

    @NotNull(message = "Channel id cannot null.")
    UUID channelId,

    @NotNull(message = "Last read time cannot null.")
    Instant lastReadAt
) {

}
