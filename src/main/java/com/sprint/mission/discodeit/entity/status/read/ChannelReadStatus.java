package com.sprint.mission.discodeit.entity.status.read;

import java.time.Instant;
import java.util.UUID;

public record ChannelReadStatus (
        UUID channelId,
        Instant lastReadAt
){
}
