package com.sprint.mission.discodeit.entity.Dto;

import com.sprint.mission.discodeit.entity.ReadStatus;

import java.time.Instant;
import java.util.UUID;

public record ReadStatusDto(
        UUID userId,
        Instant createdAt,
        Instant updatedAt
) {
    public static ReadStatusDto from(ReadStatus readStatus) {
        return new ReadStatusDto(readStatus.getUserId(), readStatus.getCreatedAt(), readStatus.getUpdatedAt());
    }
}
