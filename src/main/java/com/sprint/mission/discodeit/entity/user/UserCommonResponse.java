package com.sprint.mission.discodeit.entity.user;

import java.util.UUID;

public record UserCommonResponse (
        UUID id,
        String userName,
        String userEmail
) {
}
