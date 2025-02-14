package com.sprint.mission.discodeit.dto;

import com.sprint.mission.discodeit.domain.ChannelType;

import java.util.List;
import java.util.UUID;

public record PublicChannelRequest(
        String name,
        String description,
        UUID owner,
        ChannelType channelType
) {
}
