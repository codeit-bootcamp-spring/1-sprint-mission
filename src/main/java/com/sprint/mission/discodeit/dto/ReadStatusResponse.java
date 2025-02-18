package com.sprint.mission.discodeit.dto;

import com.sprint.mission.discodeit.entity.ReadStatus;

import java.time.Instant;
import java.util.UUID;

public record ReadStatusResponse(
        UUID id,
        Instant createdAt,
        Instant updatedAt,
        UUID userId,
        UUID channelId
) {
    public static ReadStatusResponse EntityToDto(ReadStatus readStatus) {
        return new ReadStatusResponse(
                readStatus.getId(),
                readStatus.getCreatedAt(),
                readStatus.getUpdatedAt(),
                readStatus.getUserId(),
                readStatus.getChannelId()
        );
    }
}
