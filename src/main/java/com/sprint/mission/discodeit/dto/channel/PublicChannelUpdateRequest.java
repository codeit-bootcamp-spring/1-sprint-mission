package com.sprint.mission.discodeit.dto.channel;

import java.util.UUID;

public record PublicChannelUpdateRequest(
        UUID id,
        String name,
        String introduction
) {
}
