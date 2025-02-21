package com.sprint.mission.discodeit.service.dto.userstatus;

import com.sprint.mission.discodeit.entity.UserStatus;
import java.time.Instant;
import java.util.UUID;

public record UserStatusRegisterResponse(
    UUID userId,
    Instant createAt,
    Instant updateAt,
    Instant lastAccess
) {
    public static UserStatusRegisterResponse from(UserStatus userStatus) {
        return new UserStatusRegisterResponse(
            userStatus.userId(),
            userStatus.createAt(),
            userStatus.updateAt(),
            userStatus.lastAccess()
        );
    }
}
