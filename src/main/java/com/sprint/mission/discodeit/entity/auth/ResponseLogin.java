package com.sprint.mission.discodeit.entity.auth;

import java.time.Instant;
import java.util.UUID;

public record ResponseLogin(
    UUID id,
    Instant createdAt,
    Instant updatedAt,
    String username,
    String email,
    String password,
    String profileId
) {

}
