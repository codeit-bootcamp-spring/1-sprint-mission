package com.sprint.mission.discodeit.dto.channelService;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record ChannelDTO(
        UUID id,
        ChannelType type,
        String name,
        String description,
        Instant lastMessageAt, // 가장 최근 메시지 시간
        List<UUID> participantUserIds // PRIVATE 채널 참여자 ID 리스트
) {
    public static ChannelDTO from(Channel channel, Instant lastMessageAt, List<UUID> participantUserIds) {
        return new ChannelDTO(
                channel.getId(),
                channel.getType(),
                channel.getName(),
                channel.getDescription(),
                lastMessageAt, // 가장 최근 메시지의 시간
                participantUserIds
        );
    }
}
