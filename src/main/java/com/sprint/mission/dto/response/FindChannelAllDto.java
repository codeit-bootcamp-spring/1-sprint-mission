package com.sprint.mission.dto.response;

import com.sprint.mission.entity.main.ChannelType;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record FindChannelAllDto(
    UUID id, ChannelType channelType,
    String name, String description,
    List<UUID> participantIds,
    Instant lastMessageAt) {
}
