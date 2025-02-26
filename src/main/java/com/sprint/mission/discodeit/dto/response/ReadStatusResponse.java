package com.sprint.mission.discodeit.dto.response;

import com.sprint.mission.discodeit.entity.ReadStatus;

import java.time.Instant;
import java.util.UUID;

public record ReadStatusResponse (
        UUID id,
        Instant createdAt,
        Instant updateAt,
        UUID userId,
        UUID channelId
) {
    public static ReadStatusResponse from(ReadStatus readStatus) {
        return new ReadStatusResponse(
                readStatus.getId(),
                readStatus.getCreatedAt(),
                readStatus.getUpdateAt(),
                readStatus.getUserId(),
                readStatus.getChannelId()
        );
    }
}

