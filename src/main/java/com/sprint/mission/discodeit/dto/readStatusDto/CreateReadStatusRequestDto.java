package com.sprint.mission.discodeit.dto.readStatusDto;

import java.util.UUID;

public record CreateReadStatusRequestDto(UUID userId, UUID channelId) {
}
