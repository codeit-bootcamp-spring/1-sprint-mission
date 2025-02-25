package com.sprint.mission.discodeit.dto;

import com.sprint.mission.discodeit.entity.Channel;
import lombok.AccessLevel;
import lombok.Builder;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Builder(access = AccessLevel.PRIVATE)
public record ChannelResponse(
        UUID id,
        String title,
        String description,
        Channel.ChannelType channelType,
        Instant createdAt,
        Instant updatedAt,
        Instant lastMessageTime,
        List<UUID> joinUsers
) {
    public static ChannelResponse entityToDto(Channel channel, Instant lastMessageTime, List<UUID> joinUsers) {
        return ChannelResponse.builder()
                .id(channel.getId())
                .title(channel.getTitle())
                .description(channel.getDescription())
                .channelType(channel.getChannelType())
                .createdAt(channel.getCreatedAt())
                .updatedAt(channel.getUpdatedAt())
                .lastMessageTime(lastMessageTime)
                .joinUsers(joinUsers)
                .build();
    }
}
