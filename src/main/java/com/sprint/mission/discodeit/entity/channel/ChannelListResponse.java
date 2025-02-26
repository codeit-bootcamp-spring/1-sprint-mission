package com.sprint.mission.discodeit.entity.channel;

import java.util.UUID;

public record ChannelListResponse (
        UUID channelId,
        String channelName,
        String channelType
){
}
