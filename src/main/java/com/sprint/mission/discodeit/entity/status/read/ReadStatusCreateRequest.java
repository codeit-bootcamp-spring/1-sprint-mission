package com.sprint.mission.discodeit.entity.status.read;

import java.util.UUID;

public record ReadStatusCreateRequest(
        UUID userId,
        UUID channelId
) {
}
