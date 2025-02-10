package com.sprint.mission.discodeit.dto;

import java.util.UUID;

public record ReadStatusDto(
    UUID userId,
    UUID channelId
) {
}
