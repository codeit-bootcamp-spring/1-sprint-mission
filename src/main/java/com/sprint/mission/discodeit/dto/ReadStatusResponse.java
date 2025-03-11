package com.sprint.mission.discodeit.dto;

import com.sprint.mission.discodeit.entity.ReadStatus;
import lombok.AccessLevel;
import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

@Builder(access = AccessLevel.PRIVATE)
public record ReadStatusResponse(
    UUID id,
    Instant createdAt,
    Instant updatedAt,
    UUID userId,
    UUID channelId,
    Instant lastReadAt
) {

  public static ReadStatusResponse entityToDto(ReadStatus readStatus) {
    return ReadStatusResponse.builder()
        .id(readStatus.getId())
        .userId(readStatus.getUser().getId())
        .channelId(readStatus.getChannel().getId())
        .createdAt(readStatus.getCreatedAt())
        .updatedAt(readStatus.getUpdatedAt())
        .lastReadAt(readStatus.getLastReadAt())
        .build();
  }
}
