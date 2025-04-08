package com.sprint.mission.discodeit.dto.response;

import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import java.util.UUID;

public record ReadStatusDto(
    UUID id,
    UUID userId,
    UUID channelId,

    @NotNull
    Instant lastReadAt
) {

}
