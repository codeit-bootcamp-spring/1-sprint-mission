package com.sprint.mission.discodeit.entity.user;

import java.util.UUID;

public record UserCommonResponse (
        UUID userId,
        String userName,
        String userEmail
) {
}
