package com.sprint.mission.discodeit.service.dto.userstatus;

import com.sprint.mission.discodeit.entity.UserStatus;
import java.time.Instant;
import java.util.UUID;

public record UserStatusUpdateResponse(
    UUID userId,
    Instant createAt,
    Instant updateAt,
    Instant lastAccess
) {

    public static UserStatusUpdateResponse from(UserStatus userStatus) {
        return new UserStatusUpdateResponse(
            userStatus.userId(),
            userStatus.createAt(),
            userStatus.updateAt(),
            userStatus.lastAccess()
        );
    }
}
