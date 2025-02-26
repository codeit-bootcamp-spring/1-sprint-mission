package com.sprint.mission.discodeit.dto.channel;

import com.sprint.mission.discodeit.entity.Channel;

import com.sprint.mission.discodeit.entity.ChannelType;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record ChannelFindAllResponse(
    UUID id,
    ChannelType type,
    String name,
    String description,
    List<UUID> participantIds,
    Instant latestMessageAt
) {

}
