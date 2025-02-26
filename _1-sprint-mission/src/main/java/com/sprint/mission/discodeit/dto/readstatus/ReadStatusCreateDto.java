package com.sprint.mission.discodeit.dto.readstatus;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;

import java.time.Instant;
import java.util.UUID;

public record ReadStatusCreateDto(
        UUID userId,
        UUID channelId,
        Instant lastReadAt
) {
}
