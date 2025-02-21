package com.sprint.mission.discodeit.dto.channel;

import com.sprint.mission.discodeit.entity.ChannelType;

import java.util.List;
import java.util.UUID;

public record PrivateChannelCreateRequest(
        ChannelType channelType,
        List<UUID> participants
) {
}
