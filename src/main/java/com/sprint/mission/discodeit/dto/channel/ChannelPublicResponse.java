package com.sprint.mission.discodeit.dto.channel;

import com.sprint.mission.discodeit.entity.ChannelType;

import java.util.UUID;

public record ChannelPublicResponse(
        ChannelType type,
        UUID ownerId,
        String channelName,
        String description
) {
}
