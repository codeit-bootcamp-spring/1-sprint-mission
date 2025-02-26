package com.sprint.mission.discodeit.entity.channel.add;

import java.util.UUID;

public record ChannelAddMessageRequest(
    UUID channelId,
    UUID messageId
) {

}
