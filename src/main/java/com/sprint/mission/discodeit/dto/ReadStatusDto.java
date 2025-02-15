package com.sprint.mission.discodeit.dto;

import com.sprint.mission.discodeit.entity.ReadStatus;

import java.util.UUID;

public record ReadStatusDto(
        UUID channelId,
        UUID userId
) {
    public static ReadStatusDto from(UUID channelId, UUID userId) {
        return new ReadStatusDto(channelId, userId);
    }
}
