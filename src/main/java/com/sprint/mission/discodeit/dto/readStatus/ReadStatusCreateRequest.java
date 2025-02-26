package com.sprint.mission.discodeit.dto.readStatus;

import java.time.Instant;
import java.util.UUID;

public record ReadStatusCreateRequest(
    // 유저 id
    UUID userId,
    // 채널 id
    UUID channelId,
    // 마지막 읽은 시간
    Instant lastReadTime
) {

}
