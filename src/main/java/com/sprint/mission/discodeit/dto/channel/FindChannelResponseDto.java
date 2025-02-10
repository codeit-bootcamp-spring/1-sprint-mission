package com.sprint.mission.discodeit.dto.channel;

import com.sprint.mission.discodeit.entity.Channel;

import java.time.Instant;
import java.util.List;

public record FindChannelResponseDto(
    String channelId,
    String channelName,
    String serverId,
    Channel.ChannelType channelType,
    Instant createdAt,
    Instant lastMessagedAt,
    List<String> userIds,
    int maxNumberOfPeople
) {
  public static FindChannelResponseDto from(Channel channel, Instant lastMessagedAt, List<String> userIds){
    boolean isPrivate = channel.getIsPrivate();
    return new FindChannelResponseDto(
        channel.getUUID(),
        isPrivate ? null : channel.getChannelName(),
        channel.getServerUUID(),
        channel.getChannelType(),
        channel.getCreatedAt(),
        lastMessagedAt,
        userIds,
        channel.getMaxNumberOfPeople()
    );
  }
}
