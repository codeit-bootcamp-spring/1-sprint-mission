package com.sprint.mission.discodeit.entity.Dto;

import com.sprint.mission.discodeit.entity.ReadStatus;

import java.time.Instant;
import java.util.UUID;

public record ReadStatusDto(
        UUID id,
        Instant createdAt,
        Instant updatedAt
) {
    public static ReadStatusDto from(ReadStatus readStatus) {
        return new ReadStatusDto(readStatus.getId(), readStatus.getCreatedAt(), readStatus.getUpdatedAt());
    }
}
