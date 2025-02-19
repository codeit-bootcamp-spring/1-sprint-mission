package com.sprint.mission.discodeit.entity.channel;

import com.sprint.mission.discodeit.entity.user.User;

import java.util.UUID;

public record ChannelResponse (
        UUID channelId,
        String channelName,
        String ChannelType,
        User owner

) {
}
