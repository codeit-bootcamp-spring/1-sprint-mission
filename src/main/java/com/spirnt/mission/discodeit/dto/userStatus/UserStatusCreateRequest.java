package com.spirnt.mission.discodeit.dto.userStatus;

import com.spirnt.mission.discodeit.enity.UserStatusType;
import java.time.Instant;
import java.util.UUID;

public record UserStatusCreateRequest(
    UUID userId,
    UserStatusType type,
    Instant lastSeenAt
) {

}
