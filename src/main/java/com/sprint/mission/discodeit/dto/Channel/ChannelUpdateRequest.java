package com.sprint.mission.discodeit.dto.Channel;

import com.sprint.mission.discodeit.entity.ChannelType;

public record ChannelUpdateRequest(
        String name,
        String description,
        ChannelType channelType
) {
}