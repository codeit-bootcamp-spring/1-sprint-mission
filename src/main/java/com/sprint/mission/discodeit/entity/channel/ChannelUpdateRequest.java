package com.sprint.mission.discodeit.entity.channel;

import com.sprint.mission.discodeit.entity.user.User;

import java.util.UUID;

public record ChannelUpdateRequest(
        UUID channelUUID,
        String channelName,
        String channelType,
        User owner

) {
}
