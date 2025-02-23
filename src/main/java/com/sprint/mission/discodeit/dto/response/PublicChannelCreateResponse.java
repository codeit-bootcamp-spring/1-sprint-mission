package com.sprint.mission.discodeit.dto.response;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;

import java.util.UUID;

public record PublicChannelCreateResponse(
        UUID id,
        ChannelType type,
        String name,
        String description
) {
    public static PublicChannelCreateResponse from(Channel channel) {
        return new PublicChannelCreateResponse(
                channel.getId(),
                channel.getType(),
                channel.getName(),
                channel.getDescription()
        );
    }
}