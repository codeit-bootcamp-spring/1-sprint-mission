package com.sprint.mission.discodeit.dto.readStatus;

import java.util.UUID;

public record ReadStatusCreateDto(
        UUID channelId,
        UUID userId
) {
    public static ReadStatusCreateDto from(UUID channelId, UUID userId) {
        return new ReadStatusCreateDto(channelId, userId);
    }
}
