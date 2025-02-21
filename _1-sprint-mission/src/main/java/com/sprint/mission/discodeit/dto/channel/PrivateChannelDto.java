package com.sprint.mission.discodeit.dto.channel;

import com.sprint.mission.discodeit.entity.Channel;

import java.util.List;
import java.util.UUID;

public record PrivateChannelDto (
        Channel.ChannelType channelType,
        List<UUID> userIds
){
}
