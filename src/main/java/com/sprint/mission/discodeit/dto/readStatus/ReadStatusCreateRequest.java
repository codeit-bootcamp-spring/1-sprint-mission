package com.sprint.mission.discodeit.dto.readStatus;

import java.util.UUID;

public record ReadStatusCreateRequest(
        UUID channelId,
        UUID userId
) {
    public static ReadStatusCreateRequest from(UUID channelId, UUID userId) {
        return new ReadStatusCreateRequest(channelId, userId);
    }
}
