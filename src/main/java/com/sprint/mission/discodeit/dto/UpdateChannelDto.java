package com.sprint.mission.discodeit.dto;

import java.util.UUID;

public record UpdateChannelDto(
        UUID id,
        String name,
        String topic
) {
}
