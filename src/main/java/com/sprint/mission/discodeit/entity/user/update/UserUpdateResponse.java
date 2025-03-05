package com.sprint.mission.discodeit.entity.user.update;

import java.time.Instant;
import java.util.UUID;

public record UserUpdateResponse(
    UUID id,
    Instant createdAt,
    Instant updatedAt,
    String username,
    String email,
    String password,
    String profileId
) {

}
