package com.sprint.mission.discodeit.dto.user;

import java.time.Instant;
import java.util.UUID;

//조회
public record UserDto(
        UUID id,
        Instant createdAt,
        Instant updatedAt,
        String username,
        String email,
        String phoneNumber,
        String address,
        UUID profileId,
        Boolean online
) {
}
