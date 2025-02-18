package com.sprint.mission.discodeit.dto.channel.request;

import java.util.UUID;

public record DeleteChannelRequest(
        UUID channelOwnerId,
        UUID channelId
) {
}
