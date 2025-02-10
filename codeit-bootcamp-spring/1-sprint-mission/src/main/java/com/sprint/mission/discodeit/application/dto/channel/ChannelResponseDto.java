package com.sprint.mission.discodeit.application.dto.channel;

import com.sprint.mission.discodeit.domain.channel.Channel;
import java.util.UUID;

public record ChannelResponseDto(
        UUID channelId,
        String name,
        String subject,
        String channelType
) {

    public static ChannelResponseDto from(Channel channel) {
        return new ChannelResponseDto(
                channel.getId(),
                channel.getName(),
                channel.getSubject(),
                channel.getType()
        );
    }
}
