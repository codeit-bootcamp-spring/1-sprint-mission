package com.sprint.mission.discodeit.dto.channel;

import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Channel;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record ChannelDTO(
    UUID channelId,
    ChannelType type,
    String channelName,
    String description,
    List<UUID> participantIds,
    Instant lastMessageAt
) {

  public static ChannelDTO fromEntity(Channel channel, List<UUID> participantIds,
      Instant lastMessageAt) {
    return new ChannelDTO(
        channel.getId(),
        channel.getType(),
        channel.getName(),
        channel.getDescription(),
        participantIds,
        lastMessageAt
    );
  }
}
