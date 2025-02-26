package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.channel.ChannelDto;
import com.sprint.mission.discodeit.entity.Channel;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class ChannelMapper {

  public ChannelDto channelEntityToDto(Channel channel, List<UUID> participantIds,
      Instant lastMessageAt) {
    return new ChannelDto(
        channel.getId(),
        channel.getName(),
        channel.getDescription(),
        channel.getChannelType(),
        participantIds,
        lastMessageAt,
        channel.getCreatedAt(),
        channel.getUpdatedAt()
    );
  }
}
