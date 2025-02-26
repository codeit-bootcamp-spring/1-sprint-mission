package com.sprint.mission.discodeit.dto.channel;

import com.sprint.mission.discodeit.entity.Channel;

import java.time.Instant;
import java.util.List;

public record FindChannelResponseDto(
    String id,
    Channel.ChannelType type,
    String name,
    String description,
    List<String> participantIds,
    Instant lastMessagedAt
) {
}
