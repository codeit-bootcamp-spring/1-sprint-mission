package com.sprint.mission.discodeit.dto.readStatus;

import com.sprint.mission.discodeit.entity.status.ReadStatus;

import java.time.Instant;

public record ReadStatusDto(
    String id,
    String channelId,
    String userId,
    Instant createdAt,
    Instant updatedAt,
    Instant lastReadAt,
    boolean isNewMessage
) {

  public static ReadStatusDto from(ReadStatus readStatus, boolean isNewMessage) {
    return new ReadStatusDto(
        readStatus.getId(),
        readStatus.getChannelId(),
        readStatus.getUserId(),
        readStatus.getCreatedAt(),
        readStatus.getUpdatedAt(),
        readStatus.getLastReadAt(),
        isNewMessage
    );
  }
}
