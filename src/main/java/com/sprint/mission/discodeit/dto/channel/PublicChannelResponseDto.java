package com.sprint.mission.discodeit.dto.channel;

import com.sprint.mission.discodeit.entity.Channel;

import java.time.Instant;

public record PublicChannelResponseDto(
    String channelId,
    String serverId,
    Channel.ChannelType channelType,
    String channelName,
    Instant createdAt,
    boolean isPrivate
) {
}
