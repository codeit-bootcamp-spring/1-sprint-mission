package com.sprint.mission.dto.request;

import com.sprint.mission.entity.main.Channel;
import com.sprint.mission.entity.main.ChannelType;
import java.util.List;
import java.util.UUID;

public record PrivateChannelCreateDTO(
    List<UUID> participantIds
) {
  public Channel toChannel() {
    return new Channel(null, null, ChannelType.PRIVATE);
  }
}
