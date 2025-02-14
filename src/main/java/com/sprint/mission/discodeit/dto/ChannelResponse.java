package com.sprint.mission.discodeit.dto;

import com.sprint.mission.discodeit.domain.ChannelType;
import com.sprint.mission.discodeit.entity.Channel;

import javax.naming.directory.NoSuchAttributeException;
import java.util.List;
import java.util.UUID;

public record ChannelResponse(
        String name,
        String description,
        List<UUID> member,
        UUID owner,
        ChannelType channelType
) {
    public static ChannelResponse fromEntity(Channel channel){
        return new ChannelResponse(
            channel.getName(),
            channel.getDescription(),
            channel.getMember(),
            channel.getOwner(),
            channel.getChannelType()
        );
    }
}
