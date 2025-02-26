package com.sprint.mission.discodeit.dto.channel;

import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.User;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record ChannelFindResponse(
        ChannelType type,
        UUID chnnelId,
        UUID ownerId,
        String channelName,
        String description,
        Instant updatedAt,
        List<UUID> participants,
        Instant latestMessageTime // 최근 메세지 시간 정보 포함하기
) {
}

