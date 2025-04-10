package com.sprint.mission.discodeit.dto.request;

import com.sprint.mission.discodeit.entity.Channel;

import java.util.List;
import java.util.UUID;

public record ChannelRequest(
        String name,
        String description,
//        List<UUID> member,
//        UUID owner,
        Channel.ChannelType channelType
) {
}