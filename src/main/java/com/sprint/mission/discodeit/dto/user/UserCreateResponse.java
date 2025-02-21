package com.sprint.mission.discodeit.dto.user;

import java.time.Instant;
import java.util.UUID;

public record UserCreateResponse (
        UUID id,
        String username,
        String email,
        Instant createAt,
        UUID userStatus,
        UUID profileId
){
}
