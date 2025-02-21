package com.sprint.mission.discodeit.dto.channel;

import com.sprint.mission.discodeit.entity.Channel;

import java.time.Instant;

public record UpdateChannelResponseDto(
    String id,
    Instant createdAt,
    Instant updatedAt,
    Channel.ChannelType type,
    String name,
    String description
) {
}
