package com.sprint.mission.discodeit.dto;

import com.sprint.mission.discodeit.entity.ReadStatus;

import java.time.Instant;
import java.util.UUID;

public record ReadStatusDto(UUID id, UUID userId, UUID channelId, Instant lastReadAt, Instant createdAt, Instant updatedAt) {
    public ReadStatusDto(ReadStatus readStatus){
        this(readStatus.getId(), readStatus.getUserId(), readStatus.getChannelId(), readStatus.getLastReadAt(), readStatus.getCreatedAt(), readStatus.getUpdatedAt());
    }
    public ReadStatusDto(ReadStatus readStatus, Instant lastReadAt) {
        this(readStatus.getId(), readStatus.getUserId(), readStatus.getChannelId(), lastReadAt, readStatus.getCreatedAt(), readStatus.getUpdatedAt());
    }
}
