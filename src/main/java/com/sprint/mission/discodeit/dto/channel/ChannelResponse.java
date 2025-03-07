package com.sprint.mission.discodeit.dto.channel;

import com.sprint.mission.discodeit.dto.user.UserResponse;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record ChannelResponse(UUID channelId, String channel, Boolean isPrivate,
                              Instant lastMessageTime, List<UserResponse> userList) {

  public static ChannelResponse fromEntity(Channel channel) {
    return new ChannelResponse(
        channel.getId(),
        channel.getChannelName(),
        channel.isPrivate(),
        channel.getLastMessageTime(),
        channel.getUsers().stream().map(UserResponse::fromEntity).toList());
  }
}
