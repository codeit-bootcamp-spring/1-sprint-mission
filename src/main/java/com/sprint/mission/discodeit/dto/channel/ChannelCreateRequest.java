package com.sprint.mission.discodeit.dto.channel;

import com.sprint.mission.discodeit.entity.ChannelType;

public record ChannelCreateRequest(
    String name,  // channelName에서 name으로 변경
    String description,
    ChannelType type
) {

  public static ChannelCreateRequest publicChannel(String name, String description) {
    return new ChannelCreateRequest(name, description, ChannelType.PUBLIC);
  }

  public ChannelCreateRequest(String name, String description) {  // channelName에서 name으로 변경
    this(name, description, ChannelType.PUBLIC);
  }
}