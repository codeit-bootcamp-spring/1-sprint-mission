package com.sprint.mission.discodeit.dto.response;

import com.sprint.mission.discodeit.entity.ReadStatus;
import java.time.Instant;
import java.util.UUID;

/*
  private UUID id;
  private Instant createdAt;
  private Instant updatedAt;
  //
  private UUID userId;
  private UUID channelId;
  private Instant lastReadAt;
 */
public record ReadStatusResponse(
    UUID id,
    Instant createdAt,
    Instant updatedAt,
    UUID userId,
    UUID channelId,
    Instant LastReadAt
) {

  public static ReadStatusResponse from(ReadStatus readStatus) {
    return new ReadStatusResponse(
        readStatus.getId(),
        readStatus.getCreatedAt(),
        readStatus.getUpdatedAt(),
        readStatus.getUserId(),
        readStatus.getChannelId(),
        readStatus.getLastReadAt()
    );
  }
}
