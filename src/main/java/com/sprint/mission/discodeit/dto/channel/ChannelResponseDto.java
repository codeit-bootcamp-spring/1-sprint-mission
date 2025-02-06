package com.sprint.mission.discodeit.dto.channel;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelCategory;
import com.sprint.mission.discodeit.entity.ChannelType;

public record ChannelResponseDto(
        //채널 id
        String id,
        //채널명
        String channelName,
        //채널 종류 - 음성, 텍스트
        ChannelCategory channelCategory,
        //채널 공개 여부
        ChannelType channelType,

        String description
) {
    public static ChannelResponseDto from(Channel channel) {
        return new ChannelResponseDto(channel.getId(), channel.getChannelName(), channel.getChannelCategory(), channel.getChannelType(), channel.getDescription());
    }
}
