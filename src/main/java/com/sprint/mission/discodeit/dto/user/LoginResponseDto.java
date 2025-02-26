package com.sprint.mission.discodeit.dto.user;

import java.time.Instant;

public record LoginResponseDto(
    String id,
    Instant createdAt,
    Instant updatedAt,
    String username,
    String email,
    String password,
    String profileId
) {
}
