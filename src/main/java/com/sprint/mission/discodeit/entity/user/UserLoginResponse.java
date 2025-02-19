package com.sprint.mission.discodeit.entity.user;

import java.util.UUID;

public record UserLoginResponse(
        UUID userId,
        String userName,
        String userEmail,
        UserRole userRole
) {
}
