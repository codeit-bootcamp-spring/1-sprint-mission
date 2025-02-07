package com.sprint.mission.service.dto.request;

import com.sprint.mission.entity.ChannelType;
import lombok.Getter;

import java.util.UUID;

@Getter
public class ChannelDto {

    private UUID channelId;
    private ChannelType channelType;
    private String description;
    private String name;
}
