package com.sprint.mission.discodeit.dto.readStatus;

import java.time.Instant;

public record CreateReadStatusDto(
    String channelId,
    String userId,
    Instant lastReadAt
) {

}
