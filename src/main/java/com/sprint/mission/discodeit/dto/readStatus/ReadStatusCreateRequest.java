package com.sprint.mission.discodeit.dto.readStatus;

import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import java.util.UUID;

public record ReadStatusCreateRequest(

    @NotNull
    UUID channelId,

    @NotNull
    UUID userId,

    @NotNull
    Instant lastReadAt
) {

  public static ReadStatusCreateRequest from(UUID channelId, UUID userId, Instant lastReadAt) {
    return new ReadStatusCreateRequest(channelId, userId, lastReadAt);
  }
}
