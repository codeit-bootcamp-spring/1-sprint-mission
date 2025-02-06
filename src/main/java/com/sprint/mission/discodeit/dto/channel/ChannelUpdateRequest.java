package com.sprint.mission.discodeit.dto.channel;

import com.sprint.mission.discodeit.entity.ChannelType;

import java.util.List;
import java.util.UUID;

public record ChannelUpdateRequest(
        UUID id,
        String name,
        String description,
        ChannelType channelType,
        List<UUID> participants
) {
}