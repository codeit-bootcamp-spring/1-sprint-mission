package com.spirnt.mission.discodeit.cache.event;

import com.spirnt.mission.discodeit.entity.ChannelType;
import java.util.List;
import java.util.UUID;

public record ChannelDeleteEvent(
    ChannelType channelType,
    List<UUID> participantIds
) {

}
