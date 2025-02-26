package com.sprint.mission.discodeit.dto.user.response;

import java.util.UUID;

public record UpdateUserResponse(
        UUID userId,
        String name,
        String nickname,
        String email,
        String password
) {
}
