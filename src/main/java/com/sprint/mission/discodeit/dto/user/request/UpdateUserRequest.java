package com.sprint.mission.discodeit.dto.user.request;

import java.util.UUID;

public record UpdateUserRequest(
        UUID userId,
        String name,
        String nickname,
        String email,
        String password
) {
}
