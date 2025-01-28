package com.sprint.mission.discodeit.service.channel;

import com.sprint.mission.discodeit.entity.channel.Channel;
import com.sprint.mission.discodeit.entity.channel.dto.ChannelResponse;
import com.sprint.mission.discodeit.entity.channel.dto.ChannelResponse.Builder;

public class ChannelConverter {

    public ChannelResponse toDto(Channel channel) {
        var channelResponse = new Builder()
                .channelId(channel.getId())
                .channelName(channel.getChannelName())
                .creator(channel.getCreatorName())
                .status(channel.getStatus().getDescription())
                .build();

        return channelResponse;
    }
}
