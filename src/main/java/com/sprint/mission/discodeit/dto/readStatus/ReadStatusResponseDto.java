package com.sprint.mission.discodeit.dto.readStatus;

import com.sprint.mission.discodeit.entity.status.ReadStatus;

import java.time.Instant;

public record ReadStatusResponseDto(
    String id,
    String channelId,
    String userId,
    Instant createdAt,
    Instant updatedAt,
    Instant lastReadAt,
    boolean isNewMessage
) {

  public static ReadStatusResponseDto from(ReadStatus readStatus, boolean isNewMessage) {
    return new ReadStatusResponseDto(
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
