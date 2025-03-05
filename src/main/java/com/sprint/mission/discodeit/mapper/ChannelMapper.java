package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.channel.ChannelDto;
import com.sprint.mission.discodeit.dto.channel.response.FindChannelResponse;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class ChannelMapper {

  public Channel toEntity(String name, String description, ChannelType channelType) {
    return Channel.of(name, description, channelType);
  }

  public ChannelDto toChannelDto(Channel channel) {
    return new ChannelDto(channel.getId(), channel.getCreatedAt(), channel.getUpdatedAt(),
        channel.getChannelType(), channel.getName(), channel.getDescription());
  }

  public FindChannelResponse toFindChannelResponse(Channel channel, Instant lastMessageAt,
      List<UUID> channelUsersIdList) {
    return new FindChannelResponse(channel.getId(), channel.getChannelType(),
        channel.getName(), channel.getDescription(), channelUsersIdList, lastMessageAt);
  }
}
