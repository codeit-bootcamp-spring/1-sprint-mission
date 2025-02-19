package com.sprint.mission.discodeit.dto;

import com.sprint.mission.discodeit.domain.ChannelType;

import java.util.List;
import java.util.UUID;

public record ChannelRequest(
        String name,
        String description,
        List<UUID> member,
        UUID owner,
        ChannelType channelType
) {
}
