package com.sprint.mission.discodeit.dto.readStatus;

import java.util.UUID;

public record CreateReadStatusRequestDto(UUID userId, UUID channelId) {
}
