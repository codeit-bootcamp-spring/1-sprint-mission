package com.sprint.mission.discodeit.dto.user;

import java.time.Instant;
import java.util.UUID;

public record UserStatusUpdateRequest(
        // isOnline은 UserStatus 내부에서만 변경한다고 생각한다.
        UUID UserStatusId,
        Instant lastConnectTime
) {
}
