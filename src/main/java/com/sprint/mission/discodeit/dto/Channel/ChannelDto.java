package com.sprint.mission.discodeit.dto.Channel;

import com.sprint.mission.discodeit.entity.ChannelType;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record ChannelDto (
    UUID id,
    String name,
    String description,
    ChannelType channelType,
    Instant createdAt,
    Instant updatedAt,
    List<UUID> participantIds,
    Instant lastMessageTime
) {}
