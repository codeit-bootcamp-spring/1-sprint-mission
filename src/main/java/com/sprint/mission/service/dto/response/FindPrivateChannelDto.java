package com.sprint.mission.service.dto.response;

import com.sprint.mission.entity.Channel;
import com.sprint.mission.entity.ChannelType;
import com.sprint.mission.entity.User;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class FindPrivateChannelDto implements FindChannelDto {


    private final ChannelType channelType;
    private final String description;
    private final String name;
    private final List<UUID> userIdList;
    private final Instant lastMessageTime;

    public FindPrivateChannelDto(Channel channel) {
        this.channelType = channel.getChannelType();
        this.description = channel.getDescription();
        this.name = channel.getName();
        this.userIdList =channel.getUserList().stream()
                .map(User::getId)
                .collect(Collectors.toCollection(ArrayList::new));
        this.lastMessageTime = channel.getLastMessageTime();
    }
}
