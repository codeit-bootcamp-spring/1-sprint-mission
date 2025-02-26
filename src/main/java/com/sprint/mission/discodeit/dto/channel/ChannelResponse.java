package com.sprint.mission.discodeit.dto.channel;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;

import java.time.Instant;
import java.util.UUID;

public record ChannelResponse(
        UUID id,
        ChannelType type,
        String channelName,
        String description,
        Instant createdAt,
        Instant updatedAt
) {
    public static ChannelResponse from(Channel channel) {
        return new ChannelResponse(
                channel.getId(),
                channel.getType(),
                channel.getChannelName(),
                channel.getDescription(),
                channel.getCreatedAt(),
                channel.getUpdatedAt()
        );
    }
}