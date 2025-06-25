package com.sprint.mission.discodeit.event;

import java.util.List;
import java.util.UUID;

import com.sprint.mission.discodeit.dto.data.ChannelDto;

public record PrivateChannelCreatedEvent(ChannelDto channel, List<UUID> participantIds) {
  
}
