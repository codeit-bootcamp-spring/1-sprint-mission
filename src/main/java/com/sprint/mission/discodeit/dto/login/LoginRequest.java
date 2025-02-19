package com.sprint.mission.discodeit.dto.login;

import java.util.UUID;

public record LoginRequest(
        UUID userID,
        String userName,
        String password
) {
}
