package com.sprint.mission.discodeit.dto.channel;

import com.sprint.mission.discodeit.entity.Channel;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record ChannelDetailDto(
        UUID id,
        Instant createdAt,
        Instant updatedAt,
        Channel.Type type,
        String name,
        String description,
        Instant latestMessageTime,
        List<UUID> userIdList
) {
    public static ChannelDetailDto of(UUID id, Instant createdAt, Instant updatedAt, Channel.Type type,
                                      String name, String description, Instant latestMessageTime, List<UUID> userIdList) {
        return new ChannelDetailDto(id, createdAt, updatedAt, type, name, description, latestMessageTime, userIdList);
    }
}
