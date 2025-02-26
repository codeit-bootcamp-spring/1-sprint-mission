package com.sprint.mission.discodeit.dto.channel;

import com.sprint.mission.discodeit.entity.Channel;

import java.time.Instant;
import java.util.List;

public record FindChannelResponseDto(
    String channelId,
    String channelName,
    String serverId,
    Channel.ChannelType channelType,
    boolean isPrivate,
    Instant createdAt,
    Instant lastMessagedAt,
    List<String> userIds,
    int maxNumberOfPeople
) {
}
