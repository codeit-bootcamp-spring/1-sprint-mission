package com.sprint.mission.discodeit.service.dto.userstatus;

import com.sprint.mission.discodeit.entity.UserStatus;
import java.time.Instant;
import java.util.UUID;

public record UserStatusDeleteResponse(
    UUID userId,
    Instant createAt,
    Instant updateAt,
    Instant lastAccess
) {

    public static UserStatusDeleteResponse from(UserStatus userStatus) {
        return new UserStatusDeleteResponse(
            userStatus.userId(),
            userStatus.createAt(),
            userStatus.updateAt(),
            userStatus.lastAccess()
        );
    }
}
