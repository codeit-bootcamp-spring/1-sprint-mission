package com.sprint.mission.discodeit.event;

import com.sprint.mission.discodeit.dto.ChannelDto;
import java.util.List;
import java.util.UUID;

public record PrivateChannelCreatedEvent(
    ChannelDto channelDto,
    List<UUID> participantIds
) {

  public static PrivateChannelCreatedEvent of(ChannelDto channelDto, List<UUID> participantIds) {
    return new PrivateChannelCreatedEvent(channelDto, participantIds);
  }
}
