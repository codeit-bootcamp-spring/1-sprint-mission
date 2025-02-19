package com.sprint.mission.discodeit.dto.channel;

import com.sprint.mission.discodeit.entity.Channel;

import java.time.Instant;
import java.util.UUID;

public record ChannelResponse(
        UUID id,
        String channelName,
        String description,
        Instant createdAt
) {
    public static ChannelResponse from(Channel channel) {
        return new ChannelResponse(
                channel.getId(),
                channel.getChannelName(),
                channel.getDescription(),
                channel.getCreatedAt()
        );
    }
}