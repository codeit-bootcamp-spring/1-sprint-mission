package com.sprint.mission.dto.request;

import com.sprint.mission.entity.main.ChannelType;
import lombok.Getter;

import java.util.UUID;

public record ChannelDtoForRequest (
    ChannelType channelType,
    String newName,
    String newDescription){
}
