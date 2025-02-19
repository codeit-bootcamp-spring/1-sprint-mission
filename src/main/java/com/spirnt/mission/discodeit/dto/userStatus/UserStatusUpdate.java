package com.spirnt.mission.discodeit.dto.userStatus;

import com.spirnt.mission.discodeit.enity.UserStatusType;

import java.time.Instant;

public record UserStatusUpdate(
        UserStatusType type,
        Instant lastSeenAt
) {
}
