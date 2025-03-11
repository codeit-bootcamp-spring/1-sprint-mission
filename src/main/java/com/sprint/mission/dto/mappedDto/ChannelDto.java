package com.sprint.mission.dto.mappedDto;

import com.sprint.mission.entity.main.ChannelType;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record ChannelDto(
        UUID id,
        ChannelType channelType,
        String name,
        String description,
        List<UserDto> participants,
        Instant lastMessageAt) {
}
