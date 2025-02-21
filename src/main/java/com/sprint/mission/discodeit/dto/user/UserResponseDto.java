package com.sprint.mission.discodeit.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;

public record UserResponseDto(

    String id,
    Instant createdAt,
    Instant updatedAt,
    String username,
    String email,
    String profileId,
    boolean online
) {
}
