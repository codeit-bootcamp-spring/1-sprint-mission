package com.sprint.mission.discodeit.dto.channel;

import com.sprint.mission.discodeit.entity.ChannelCategory;
import com.sprint.mission.discodeit.entity.ChannelType;

import java.util.List;

public record CreateChannelDto(
        //채널명
        String channelName,
        ChannelType channelType,
        ChannelCategory channelCategory,
        String description

) {
}
