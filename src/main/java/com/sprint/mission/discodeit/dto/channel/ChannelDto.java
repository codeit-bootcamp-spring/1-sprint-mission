package com.sprint.mission.discodeit.dto.channel;

import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Channel;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record ChannelDto(
    UUID channelId,
    ChannelType type,
    String channelName,
    String description,
    List<UUID> participantIds,
    Instant lastMessageAt
) {

  public static ChannelDto fromEntity(Channel channel, List<UUID> participantIds,
      Instant lastMessageAt) {
    return new ChannelDto(
        channel.getId(),
        channel.getType(),
        channel.getName(),
        channel.getDescription(),
        participantIds,
        lastMessageAt
    );
  }
}
