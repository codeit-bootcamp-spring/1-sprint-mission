package com.sprint.mission.discodeit.entity.channel;

import java.util.UUID;

public record ChannelUpdateResponse(
        UUID channelId,
        String channelName,
        UUID ownerUserId,
        String channelType
) {
}
