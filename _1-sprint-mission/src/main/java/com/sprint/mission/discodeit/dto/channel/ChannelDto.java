package com.sprint.mission.discodeit.dto.channel;

import com.sprint.mission.discodeit.entity.Channel;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record ChannelDto(
        UUID id,
        Channel.ChannelType channelType,
        String name,
        String description,
        List<UUID> participantIds,
        Instant lastMessageAt
) {
}
