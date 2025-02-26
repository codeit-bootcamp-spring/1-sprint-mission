package com.sprint.mission.discodeit.dto.channel;

import com.sprint.mission.discodeit.entity.Channel;

import java.time.Instant;
import java.util.List;

public record PrivateChannelResponseDto(
    String channelId,
    String serverId,
    Channel.ChannelType channelType,
    Instant createdAt,
    boolean isPrivate,
    List<String> participants

){
}
