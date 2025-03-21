package com.sprint.mission.dto.request;

import com.sprint.mission.entity.main.Channel;
import com.sprint.mission.entity.main.ChannelType;

public record PublicChannelCreateDTO(
    String name,
    String description
) {
  public Channel toChannel() {
    return new Channel(name, description, ChannelType.PUBLIC);
  }
}
