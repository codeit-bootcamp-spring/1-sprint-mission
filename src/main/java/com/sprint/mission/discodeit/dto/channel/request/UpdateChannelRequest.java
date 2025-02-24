package com.sprint.mission.discodeit.dto.channel.request;

import java.util.UUID;

public record UpdateChannelRequest(
        UUID channelOwnerId,
        String name,
        String description
) {
}
