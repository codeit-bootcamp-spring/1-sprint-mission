package com.sprint.mission.discodeit.dto.channel.response;

import java.util.UUID;

public record CreateChannelResponse(
        UUID channelId,
        String name,
        String description
) {
}
