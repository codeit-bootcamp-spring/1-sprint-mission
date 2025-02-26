package com.sprint.mission.discodeit.dto.channel;

import com.sprint.mission.discodeit.entity.Channel;

public record PublicChannelCreateDto(
        String channelName,
        String description
){
}
