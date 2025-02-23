package com.sprint.mission.discodeit.dto.response;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;

import java.util.UUID;

public record PrivateChannelCreateResponse(
        UUID id,
        ChannelType type
) {
    public static PrivateChannelCreateResponse from(Channel channel) {
        return new PrivateChannelCreateResponse(
                channel.getId(),
                channel.getType()
        );
    }
}