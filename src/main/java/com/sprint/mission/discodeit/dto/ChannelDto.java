package com.sprint.mission.discodeit.dto;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;

import java.time.Instant;
import java.util.UUID;

public record ChannelDto(UUID id, String name, ChannelType type, Instant createdAt, Instant updatedAt) {
    public ChannelDto(Channel channel){
        this(channel.getId(), channel.getName(), channel.getType(), channel.getCreatedAt(), channel.getUpdatedAt());
    }
    public ChannelDto(String name, ChannelType type){
        this(null, name, type,null, null);
    }
    public ChannelDto(ChannelType type){
        this(null, null, type,null, null);
    }
    public ChannelDto(UUID id, String name){
        this(id, name, null,null, null);
    }
}