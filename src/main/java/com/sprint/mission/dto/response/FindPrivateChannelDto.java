package com.sprint.mission.dto.response;

import com.sprint.mission.entity.main.Channel;
import com.sprint.mission.entity.main.ChannelType;
import com.sprint.mission.entity.main.User;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class FindPrivateChannelDto implements FindChannelDto {

    private final UUID channelId;
    private final ChannelType channelType;
    private final String description;
    private final String name;

    public FindPrivateChannelDto(Channel channel) {
        this.channelId = channel.getId();
        this.channelType = channel.getChannelType();
        this.description = channel.getDescription();
        this.name = channel.getName();

    }
}
