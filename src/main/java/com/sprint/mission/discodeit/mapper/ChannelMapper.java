package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.channel.ChannelResponse;
import com.sprint.mission.discodeit.dto.user.UserResponse;
import com.sprint.mission.discodeit.entity.Channel;

public class ChannelMapper {

  public static ChannelResponse toDto(Channel channel) {
    return new ChannelResponse(
        channel.getId(),
        channel.getChannelName(),
        channel.isPrivate(),
        channel.getLastMessageTime(),
        channel.getUsers().stream().map(UserResponse::fromEntity).toList());
  }
}
