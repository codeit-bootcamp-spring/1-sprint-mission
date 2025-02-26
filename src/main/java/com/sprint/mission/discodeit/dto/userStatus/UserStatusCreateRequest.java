package com.sprint.mission.discodeit.dto.userStatus;

import java.util.UUID;

public record UserStatusCreateRequest(
        UUID userId
) {
    public static UserStatusCreateRequest from(UUID userId) {
        return new UserStatusCreateRequest(userId);
    }
}
