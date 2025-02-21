package com.sprint.mission.discodeit.dto.channel;

import com.sprint.mission.discodeit.entity.Channel;

public record PublicChannelDto (
        Channel.ChannelType channelType,
        String channelName,
        String description
){
}
