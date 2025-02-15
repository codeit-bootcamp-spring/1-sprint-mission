package com.sprint.mission.discodeit.dto;

import com.sprint.mission.discodeit.entity.Channel;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record ChannelResponse(
        UUID id,
        String title,
        String description,
        Channel.ChannelType channelType,
        Instant createdAt,
        Instant updatedAt,
        Instant lastMessageTime,
        List<UUID> joinUsers
) {
    public static ChannelResponse entityToDto(Channel channel, Instant lastMessageTime, List<UUID> joinUsers) {
        return new ChannelResponse(channel.getId(), channel.getTitle(), channel.getDescription(), channel.getChannelType(), channel.getCreatedAt(), channel.getUpdatedAt(), lastMessageTime, joinUsers);
    }
}
