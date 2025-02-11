package com.sprint.mission.discodeit.application.dto.channel;

import com.sprint.mission.discodeit.domain.channel.Channel;
import java.util.UUID;

public record ChannelCreateResponseDto(
        UUID channelId,
        String name,
        String subject,
        String channelType
) {

    public static ChannelCreateResponseDto from(Channel channel) {
        return new ChannelCreateResponseDto(
                channel.getId(),
                channel.getName(),
                channel.getSubject(),
                channel.getType()
        );
    }
}
