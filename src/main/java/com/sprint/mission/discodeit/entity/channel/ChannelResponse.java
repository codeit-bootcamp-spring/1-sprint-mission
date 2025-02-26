package com.sprint.mission.discodeit.entity.channel;

import com.sprint.mission.discodeit.entity.user.UserChannelOwnerResponse;

import java.util.UUID;

public record ChannelResponse (
        UUID channelId,
        String channelName,
        String channelType,
        UserChannelOwnerResponse owner

) {
}
