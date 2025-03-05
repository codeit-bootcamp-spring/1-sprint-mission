package com.sprint.mission.discodeit.entity.user.create;

import java.time.Instant;
import java.util.UUID;

public record UserCreateResponse(
    UUID id,
    Instant createdAt,
    Instant updatedAt,
    String username,
    String email,
    String password,
    String profileId
) {

}
