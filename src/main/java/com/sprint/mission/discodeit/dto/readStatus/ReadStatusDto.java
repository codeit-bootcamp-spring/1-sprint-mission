package com.sprint.mission.discodeit.dto.readStatus;

import com.sprint.mission.discodeit.entity.status.ReadStatus;

import java.time.Instant;
import java.util.UUID;

public record ReadStatusDto(
    String id,
    UUID channelId,
    UUID userId,
    Instant lastReadAt,
    boolean isNewMessage
) {

  public static ReadStatusDto from(ReadStatus readStatus, boolean isNewMessage) {
    return new ReadStatusDto(
        readStatus.getId().toString(),
        readStatus.getChannel().getId(),
        readStatus.getUser().getId(),
        readStatus.getLastReadAt(),
        isNewMessage
    );
  }
}
