package com.sprint.mission.discodeit.dto.channel;

import com.sprint.mission.discodeit.entity.ChannelType;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record ChannelResponse(
        UUID id,
        ChannelType type,
        String name,
        String introduction,
        Instant lastMessageTime,
        List<UUID> participantIds
) {
    public static ChannelResponse from(UUID id, ChannelType type, String name, String introduction, Instant lastMessageTime, List<UUID> participantIds) {
        return new ChannelResponse(id, type, name, introduction, lastMessageTime, participantIds);
    }
}
