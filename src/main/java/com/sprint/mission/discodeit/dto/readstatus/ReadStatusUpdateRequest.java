package com.sprint.mission.discodeit.dto.readstatus;

import java.time.Instant;
import java.util.UUID;

public record ReadStatusUpdateRequest(
    UUID id,
    Instant channelLastReadTimes
) {

}
