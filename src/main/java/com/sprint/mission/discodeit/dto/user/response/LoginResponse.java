package com.sprint.mission.discodeit.dto.user.response;

import java.util.UUID;

public record LoginResponse(
        UUID userId,
        String nickname,
        String email
) {
}
