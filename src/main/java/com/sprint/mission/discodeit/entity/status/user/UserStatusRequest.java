package com.sprint.mission.discodeit.entity.status.user;

import java.util.UUID;

public record UserStatusRequest(
        UUID userId,
        String userName,
        String state
) {
}
