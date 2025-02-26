package com.sprint.mission.discodeit.dto.response;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;

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
        List<UUID> userIds
) {

    public static ChannelDetailResponse of(Channel channel, Instant latestMessageTime) {
        List<UUID> userIds = channel.getUsers().values().stream()
                .map(User::getId)
                .toList();
        return new ChannelDetailResponse(
                channel.getId(),
                channel.getCreatedAt(),
                channel.getUpdatedAt(),
                channel.getType(),
                channel.getName(),
                channel.getDescription(),
                latestMessageTime,
                userIds
        );
    }
}
