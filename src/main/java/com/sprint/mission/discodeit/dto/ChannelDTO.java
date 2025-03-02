package com.sprint.mission.discodeit.dto;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record ChannelDTO(
    UUID channelId,
    UUID adminId,
    ChannelType type,
    String channelName,
    List<UUID> memberList,
    Instant lastMessageAt
) {

  public static ChannelDTO fromEntity(Channel channel) {
    return new ChannelDTO(
        channel.getId(),
        channel.getAdmin().getId(),
        channel.getType(),
        channel.getChannelName(),
        channel.getType() == ChannelType.PRIVATE ? channel.getMemberList() : List.of(),
        channel.getCreatedAt()
    );
  }
}
