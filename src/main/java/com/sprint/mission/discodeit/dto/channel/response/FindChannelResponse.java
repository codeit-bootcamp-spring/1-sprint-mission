package com.sprint.mission.discodeit.dto.channel.response;

import com.sprint.mission.discodeit.entity.ChannelType;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record FindChannelResponse(
        String name,
        String description,
        Instant lastMessageTime,
        ChannelType channelType,
        List<UUID> channelUserIdList
) {
}
