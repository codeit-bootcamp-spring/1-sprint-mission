package com.sprint.mission.discodeit.dto;

import com.sprint.mission.discodeit.entity.Channel;

import java.util.UUID;

public record PublicChannelRequest(
        String name,
        String description,
        UUID owner,
        Channel.ChannelType channelType
) {
}
