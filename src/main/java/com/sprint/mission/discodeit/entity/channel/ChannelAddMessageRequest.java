package com.sprint.mission.discodeit.entity.channel;

import java.util.UUID;

public record ChannelAddMessageRequest (
        UUID channelId,
        UUID messageId
){
}
