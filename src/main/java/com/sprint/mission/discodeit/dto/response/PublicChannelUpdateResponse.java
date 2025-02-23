package com.sprint.mission.discodeit.dto.response;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;

import java.util.UUID;

public record PublicChannelUpdateResponse(
        UUID id,
        ChannelType type,
        String name,
        String description
) {
    public static PublicChannelUpdateResponse from(Channel channel) {
        return new PublicChannelUpdateResponse(
                channel.getId(),
                channel.getType(),
                channel.getName(),
                channel.getDescription()
        );
    }
}