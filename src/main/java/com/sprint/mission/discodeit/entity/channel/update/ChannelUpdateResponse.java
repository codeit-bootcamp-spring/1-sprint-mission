package com.sprint.mission.discodeit.entity.channel.update;

import java.util.UUID;

public record ChannelUpdateResponse(
    UUID channelId,
    String channelName,
    UUID ownerUserId,
    String channelType
) {

}
