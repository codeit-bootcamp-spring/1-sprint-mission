package com.sprint.mission.discodeit.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import java.time.Instant;
import java.util.UUID;

public record ReadStatusCreateRequest(
    @NotNull(message = "User ID cannot be null")
    UUID userId,
    @NotNull(message = "Channel ID cannot be null")
    UUID channelId,
    @NotNull(message = "Last read at cannot be null")
    @PastOrPresent(message = "Last read at must be in the past or present")
    Instant lastReadAt
) {

}
