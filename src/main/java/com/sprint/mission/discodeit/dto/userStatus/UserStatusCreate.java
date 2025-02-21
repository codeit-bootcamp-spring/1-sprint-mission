package com.sprint.mission.discodeit.dto.userStatus;

import java.util.UUID;

public record UserStatusCreate(
        UUID userId
) {
    public static UserStatusCreate from(UUID userId) {
        return new UserStatusCreate(userId);
    }
}
