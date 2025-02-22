package com.sprint.mission.dto.response;

import com.sprint.mission.entity.main.Channel;
import com.sprint.mission.entity.main.ChannelType;
import lombok.Data;
import lombok.Getter;

import java.time.Instant;
import lombok.NoArgsConstructor;

@Data
public class FindPublicChannelDto implements FindChannelDto {

    private final ChannelType channelType;
    private final String description;
    private final String name;

    public FindPublicChannelDto(Channel channel) {
        this.channelType = channel.getChannelType();
        this.description = channel.getDescription();
        this.name = channel.getName();
    }
}
