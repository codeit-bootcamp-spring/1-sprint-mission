package com.sprint.mission.discodeit.dto.channel;

import com.sprint.mission.discodeit.entity.Channel;

import java.time.Instant;
import java.util.UUID;

public record ChannelDto(
        UUID id,
        String channelName,
        String description,
        Instant createdAt,
        Instant updatedAt
) {
    public static ChannelDto from(Channel channel) {
        return new ChannelDto(
                channel.getId(),
                channel.getChannelName(),
                channel.getDescription(),
                channel.getCreatedAt(),
                channel.getUpdatedAt()
        );
    }
}