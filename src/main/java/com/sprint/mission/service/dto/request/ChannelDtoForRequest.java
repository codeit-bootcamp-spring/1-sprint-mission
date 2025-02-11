package com.sprint.mission.service.dto.request;

import com.sprint.mission.entity.main.ChannelType;
import lombok.Getter;

import java.util.UUID;

@Getter
public class ChannelDtoForRequest {

    private UUID channelId;
    private ChannelType channelType;
    private String description;
    private String name;
}
