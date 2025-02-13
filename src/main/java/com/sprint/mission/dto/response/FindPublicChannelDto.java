package com.sprint.mission.dto.response;

import com.sprint.mission.entity.main.Channel;
import com.sprint.mission.entity.main.ChannelType;
import lombok.Getter;

import java.time.Instant;

@Getter
public class FindPublicChannelDto implements FindChannelDto {

    private ChannelType channelType;
    private String description;
    private String name;
    private Instant lastMessageTime;

    public FindPublicChannelDto(Channel channel) {
        this.channelType = channel.getChannelType();
        this.description = channel.getDescription();
        this.name = channel.getName();
        this.lastMessageTime = channel.getLastMessageTime();
    }

    public FindPublicChannelDto() {}


}
