package com.sprint.mission.discodeit.dto.channel;

import com.sprint.mission.discodeit.entity.ChannelType;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record ChannelDto(
        // 채널 id
        UUID channelId,
        // 채널 명 (public channel)
        String name,
        // 설명 (public channel)
        String description,
        // 타입
        ChannelType channelType,
        // 참여자 id (private channel)
        List<UUID> participantIds,
        Instant lastMessageAt,
        Instant createdAt,
        Instant updatedAt
) {
}
