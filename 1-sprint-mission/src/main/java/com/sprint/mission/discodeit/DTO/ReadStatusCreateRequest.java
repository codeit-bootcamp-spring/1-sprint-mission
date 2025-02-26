package com.sprint.mission.discodeit.DTO;

import java.util.UUID;

public record ReadStatusCreateRequest(UUID channelId, UUID userId, boolean isRead) {
}
