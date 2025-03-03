package com.sprint.mission.discodeit.dto;

import com.sprint.mission.discodeit.entity.Channel;

import java.util.List;
import java.util.UUID;

public record ChannelResponse(
    UUID id,
    String name,
    String description,
    List<UUID> member,
    UUID owner,
    Channel.ChannelType channelType
) {
    public static ChannelResponse fromEntity(Channel channel){
        return new ChannelResponse(
                channel.getId(),
                channel.getName(),
                channel.getDescription(),
                channel.getMember(),
                channel.getOwner(),
                channel.getChannelType()
        );
    }
}