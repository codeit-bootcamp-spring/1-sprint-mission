package com.sprint.mission.discodeit.dto.response;

import com.sprint.mission.discodeit.entity.Channel;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record ChannelDetailResponse(
        UUID id,
        Instant createdAt,
        Instant updatedAt,
        Channel.Type type,
        String name,
        String description,
        Instant latestMessageTime,
        List<UUID> userIdList
) {
    public static ChannelDetailResponse of(UUID id, Instant createdAt, Instant updatedAt, Channel.Type type,
                                           String name, String description, Instant latestMessageTime, List<UUID> userIdList) {
        return new ChannelDetailResponse(id, createdAt, updatedAt, type, name, description, latestMessageTime, userIdList);
    }
}
