package com.sprint.mission.discodeit.dto.userStatus;

import com.sprint.mission.discodeit.entity.UserStatus;

import java.time.Instant;
import java.util.UUID;

public record UserStatusResponse(
        UUID id,
        UUID userId,
        Instant lastActivityAt,
        boolean isCurrentlyLoggedIn,
        Instant createdAt,
        Instant updatedAt
) {
    public static UserStatusResponse from(UserStatus userStatus) {
        return new UserStatusResponse(
                userStatus.getId(),
                userStatus.getUserId(),
                userStatus.getLastActivityAt(),
                userStatus.isCurrentlyLoggedIn(),
                userStatus.getCreatedAt(),
                userStatus.getUpdatedAt()
        );
    }
}