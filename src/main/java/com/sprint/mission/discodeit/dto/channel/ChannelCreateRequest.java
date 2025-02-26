package com.sprint.mission.discodeit.dto.channel;

import com.sprint.mission.discodeit.entity.ChannelType;

public record ChannelCreateRequest(
        String channelName,
        String description,
        ChannelType type
) {
    public static ChannelCreateRequest publicChannel(String name, String description) {
        return new ChannelCreateRequest(name, description, ChannelType.PUBLIC);
    }

    public ChannelCreateRequest(String channelName, String description) {
        this(channelName, description, ChannelType.PUBLIC);
    }
}