package com.sprint.mission.discodeit.dto.readStatus;

import java.time.Instant;
import java.util.UUID;

public record ReadStatusUpdateResponse (
        UUID id,
        UUID userId,
        UUID channelId,
        Instant lastMessageReadTime
){
}
