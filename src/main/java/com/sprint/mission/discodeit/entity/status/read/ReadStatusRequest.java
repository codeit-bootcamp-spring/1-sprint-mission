package com.sprint.mission.discodeit.entity.status.read;

import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import java.util.UUID;

public record ReadStatusRequest(
    @NotNull
    UUID userId,

    @NotNull
    UUID channelId,

    @NotNull
    Instant lastReadAt
) {

}
