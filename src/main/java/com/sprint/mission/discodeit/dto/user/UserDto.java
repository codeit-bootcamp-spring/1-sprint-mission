package com.sprint.mission.discodeit.dto.user;

import java.time.Instant;
import java.util.UUID;

public record UserDto(
    UUID userId,
    Instant createdAt,
    Instant updatedAt,
    String userName,
    String email,
    UUID profileId,
    boolean isOnline
) {

}
