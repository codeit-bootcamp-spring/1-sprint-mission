package com.sprint.mission.discodeit.dto;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.User;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record ChannelFindDto(UUID id, String name, ChannelType type, Instant createdAt, Instant updatedAt, Instant recentMessageTimestamp, List<UUID> list) {
    public ChannelFindDto(Channel channel, Instant recentMessageTimestamp){
        this(channel.getId(), channel.getName(), channel.getType(), channel.getCreatedAt(), channel.getUpdatedAt(), recentMessageTimestamp, null);
    }
    public ChannelFindDto(Channel channel, Instant recentMessageTimestamp, List<UUID> list){
        this(channel.getId(), channel.getName(), channel.getType(), channel.getCreatedAt(), channel.getUpdatedAt(), recentMessageTimestamp, list);
    }
}
