package com.sprint.mission.discodeit.DTO;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public record ChannelDto(
        UUID id,
        String channelName,
        ChannelType channelType,
        Instant createdAt,
        Instant updatedAt,
        Instant latestMessageTime,
        List<UUID> userIds
) {
    public static ChannelDto fromEntity(Channel channel) {
        Instant latestMessageTime = channel.getMessageList().stream()
                .map(Message::getCreatedAt)
                .max(Instant::compareTo)
                .orElse(null);

        List<UUID> userIds = channel.getChannelType() == ChannelType.PRIVATE
                ? channel.getUserList().keySet().stream().map(User::getId).toList()
                : List.of();

        return new ChannelDto(
                channel.getId(),
                channel.getChannelName(),
                channel.getChannelType(),
                channel.getCreatedAt(),
                channel.getUpdatedAt(),
                latestMessageTime,
                userIds
        );
    }
}
