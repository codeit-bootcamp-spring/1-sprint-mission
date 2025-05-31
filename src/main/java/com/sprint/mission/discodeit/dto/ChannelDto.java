package com.sprint.mission.discodeit.dto;

import com.sprint.mission.discodeit.entity.Channel;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record ChannelDto(
    UUID id,
    Channel.Type type,
    String name,
    String description,
    List<UserDto> participants,
    Instant lastMessageAt
) {

  public static ChannelDto of(Channel channel, Instant latestMessageTime,
      List<UserDto> participants) {
    return new ChannelDto(
        channel.getId(),
        channel.getType(),
        channel.getName(),
        channel.getDescription(),
        participants,
        latestMessageTime
    );
  }
}
