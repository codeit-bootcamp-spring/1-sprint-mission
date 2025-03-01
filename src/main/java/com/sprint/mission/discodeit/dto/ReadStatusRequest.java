package com.sprint.mission.discodeit.dto;

import java.time.Instant;
import java.util.UUID;

public record ReadStatusRequest(
) {

  public record Create(
      UUID userId,
      UUID channelId,
      Instant lastReadAt
  ) {

  }

  public record Update(
      Instant newLastReadAt
  ) {

  }

}