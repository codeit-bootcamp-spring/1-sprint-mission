package com.sprint.mission.service.dto.response;

import com.sprint.mission.entity.main.Channel;
import com.sprint.mission.entity.main.ChannelType;

import java.time.Instant;

public class FindPublicChannelDto implements FindChannelDto {

    private final ChannelType channelType;
    private final String description;
    private final String name;
    private final Instant lastMessageTime;

    public FindPublicChannelDto(Channel channel) {
        this.channelType = channel.getChannelType();
        this.description = channel.getDescription();
        this.name = channel.getName();
        this.lastMessageTime = channel.getLastMessageTime();
    }
}
