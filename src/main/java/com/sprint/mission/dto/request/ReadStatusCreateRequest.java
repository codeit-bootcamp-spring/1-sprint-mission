package com.sprint.mission.dto.request;

import com.sprint.mission.entity.addOn.ReadStatus;
import java.time.Instant;
import java.util.UUID;

public record ReadStatusCreateRequest(
    UUID userId,
    UUID channelId,
    Instant lastReadAt) {

  public ReadStatus toEntity() {
    return new ReadStatus(channelId, userId, lastReadAt);
  }
}
