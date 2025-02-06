package com.sprint.mission.discodeit.dto.Channel;

import com.sprint.mission.discodeit.entity.ChannelType;

import java.util.List;
import java.util.UUID;

public record ChannelCreateRequest(
        String name,
        String description,
        ChannelType channelType,
        List<UUID> participantIds
) {
}
